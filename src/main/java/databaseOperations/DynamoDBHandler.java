package main.java.databaseOperations;

import main.java.logic.Constants;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.TransactionManager;
import com.amazonaws.services.dynamodbv2.transactions.exceptions.TransactionRolledBackException;
import com.amazonaws.services.dynamodbv2.transactions.exceptions.UnknownCompletedTransactionException;

import main.java.logic.TimeHelper;
import main.java.logic.debugging.SingletonTimer;
import main.java.databaseObjects.*;
import main.java.databaseOperations.exceptions.ItemNotFoundException;
import main.java.testing.TestHelper;
import main.java.testing.TestTableHelper;

import java.util.*;

/**
 * This class uses the Singleton pattern and handles all the intensive specifically AWS DynamoDB
 * Database operations. Uses the Java Transactional library for DynamoDB in order to increase the
 * likelihood of atomic creation.
 */
public class DynamoDBHandler {
    // Singleton Pattern!
    private static DynamoDBHandler instance;

    private TransactionManager txManager;
    public AmazonDynamoDB client;
    private Map<String, Table> tables;
    private Table databaseTable;
    private Table messageTable;
//    private Table firebaseTokenTable;

    /**
     * The get instance method for the Singleton design pattern. Ensures that only one instance of
     * the class is ever created.
     */
    static synchronized public DynamoDBHandler getInstance() throws Exception {
        if (DynamoDBHandler.instance == null) {
            DynamoDBHandler.instance = new DynamoDBHandler();
        }
        return DynamoDBHandler.instance;
    }

    /**
     * Private constructor for the DynamoDBHandler class. Handles all the initialization of the
     * connections to the database tables and the initialization of the Transaction library usage.
     */
    private DynamoDBHandler() throws Exception {
        SingletonTimer.get().pushCheckpoint("Instantiate DynamoDB Handler");
        // AWS CREDENTIALS GO HERE

        if (TestHelper.getIfTesting()) {
            SingletonTimer.get().pushCheckpoint("Connect DynamoDB Local client");
            client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration("http://localhost:" +
                            TestHelper.getPort() +  "/", "us-east-1")).build();
        }
        else {
            SingletonTimer.get().pushCheckpoint("Connect DynamoDB client");
            client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        }

        long waitTimeSeconds = 10 * 60;

        SingletonTimer.get().endAndPushCheckpoint("Verify or Create Transactions table");
        TransactionManager.verifyOrCreateTransactionTable(client, "Transactions", 10, 10, waitTimeSeconds);
        SingletonTimer.get().endAndPushCheckpoint("Verify or Create Transaction Images table");
        TransactionManager.verifyOrCreateTransactionImagesTable(client, "TransactionImages", 10, 10, waitTimeSeconds);

        SingletonTimer.get().endAndPushCheckpoint("Create the transaction manager");
        txManager = new TransactionManager(client, "Transactions", "TransactionImages");

        if (TestHelper.getIfTesting()) {
            SingletonTimer.get().endAndPushCheckpoint("Create the test database table");
            databaseTable = TestTableHelper.getInstance().createAndFillDatabaseTable(client,
                    TestHelper.getDatabaseTableJsonPath());
            SingletonTimer.get().endAndPushCheckpoint("Connect to the message table");
            messageTable = TestTableHelper.getInstance().createAndFillMessageTable(client,
                    TestHelper.getMessagesTableJsonPath());
//            SingletonTimer.get().endAndPushCheckpoint("Connect to the firebase token table");
//            firebaseTokenTable = new Table(client, Constants.firebaseTokenTableName);
        }
        else {
            SingletonTimer.get().endAndPushCheckpoint("Connect to the database table");
            databaseTable = new Table(client, Constants.databaseTableName);
            SingletonTimer.get().endAndPushCheckpoint("Connect to the message table");
            messageTable = new Table(client, Constants.messageTableName);
//            SingletonTimer.get().endAndPushCheckpoint("Connect to the firebase token table");
//            firebaseTokenTable = new Table(client, Constants.firebaseTokenTableName);
        }

        tables = new HashMap<>();
        tables.put(Constants.databaseTableName, databaseTable);
        tables.put(Constants.messageTableName, messageTable);

        SingletonTimer.get().endCheckpoints(2);
    }

    /**
     * Uses the list of database action compilers to perform the operations as indicated in the API
     * and isolates the creation between each compiler. It also tries to determine the ID as close
     * to the actual transaction as possible, in order to reduce the chance of trying to put
     * duplicate IDs in the database. Also sends notifications after the transaction is complete.
     *
     * @param databaseActionCompilers The List of compilers for all the database actions.
     * @return The first newly created ID if the transaction included a CREATE statement.
     * @throws Exception If any of the transactions fail or the pre-transaction checks fail.
     */
    public String attemptTransaction(List<DatabaseActionCompiler> databaseActionCompilers) throws Exception {
        // Marker retry implemented
        final int transactionActionLimit = 10; // Transactions can only handle 10 unique things each
        // This represents the current passoverIDs for the current transaction. Supports inputting
        // passover IDs from any other compiler. PassoverIdentifer -> passoverID.
        Map<String, String> passoverIDs = new HashMap<>();
        // This indicates created ID for current compiler and also supports passover between transactions.
        String newlyCreatedID = null; // This indicates the created ID for the current compiler.
        String returnString = null; // This indicates the created ID for the first compiler

        SingletonTimer.get().endAndPushCheckpoint("Pre-checking the actions check handlers");

        // Pre-checking all the actions to catch an error earlier.
        for (DatabaseActionCompiler databaseActionCompiler : databaseActionCompilers) {
            for (DatabaseAction databaseAction : databaseActionCompiler.getDatabaseActions()) {
                if (databaseAction.action == DBAction.UPDATESAFE) {
                    DatabaseItem databaseItem = readItem(databaseAction.getTableName(), databaseAction.primaryKey);
                    String errorMessage = databaseAction.checkHandler.isViable(databaseItem);
                    if (errorMessage != null) {
                        throw new Exception("Unable to perform database action. Item failed runtime checker: " +
                                errorMessage);
                    }
                }
            }
        }

        SingletonTimer.get().endAndPushCheckpoint("Handling the transactions");

        // List of transactions to commit all of them at the end.
        List<Transaction> transactions = new ArrayList<>();

        // For each transaction
        for (DatabaseActionCompiler databaseActionCompiler : databaseActionCompilers) {
            // Start the new transaction
            Transaction transaction = txManager.newTransaction();
            // Count how many unique actions is in the current transaction
            int uniqueActionsInCurrentTransaction = 0;

            List<DatabaseAction> databaseActions = databaseActionCompiler.getDatabaseActions();

            for (DatabaseAction databaseAction : databaseActions) {
                switch (databaseAction.action) {
                    case CREATE:
                        try {
                            // Get the ID!
                            String id = getNewID(databaseAction.itemType);
                            databaseAction.item.put("id", new AttributeValue(id));
                            databaseAction.item.put("time_created", new AttributeValue(TimeHelper.nowString()));
                            ((CreateDatabaseAction) databaseAction).updateWithIDHandler.updateWithID(databaseAction.item, id);

                            // Place in the passover identifier if applicable...
                            if (databaseActionCompiler.getPassoverIdentifier() != null) {
                                passoverIDs.put(databaseActionCompiler.getPassoverIdentifier(), id);
                            }

                            if (databaseAction.item.containsKey("username")) {
                                if (this.usernameInDatabase(databaseAction.item.get("username").getS(), databaseAction.item
                                        .get("item_type").getS())) {
                                    // This means that the database already has this username, we should not do this
                                    throw new Exception("Attempted to put repeat username: " + databaseAction.item.get
                                            ("username").getS() + " into the database!");
                                }
                            }

                            Map<String, AttributeValue> putItem = getPutItem(databaseAction,
                                    newlyCreatedID, passoverIDs);

                            Constants.debugLog("Creating item with item: " + databaseAction.item);

                            transaction.putItem(new PutItemRequest().withTableName(databaseAction.getTableName()).withItem
                                    (putItem));
                            uniqueActionsInCurrentTransaction++;
                            newlyCreatedID = id;
                            if (returnString == null) { returnString = newlyCreatedID; }

                            // Add the item to the Create object fields
                            databaseActionCompiler.getNotificationHandler().fillCreateObject(id,
                                    convertFromAttributeValueMap(databaseAction.item));
                        } catch (Exception e) {
                            Constants.debugLog("ROLLING BACK TRANSACTION FROM ERROR = " + e.getLocalizedMessage());
                            transaction.rollback();
                            for (Transaction existingTransaction : transactions) {
                                existingTransaction.rollback();
                            }
                            throw new Exception("Error while trying to create item in the database! Error: " + e
                                    .getLocalizedMessage(), e);
                        }
                        break;
                    case UPDATE:
                        try {
                            // If the primary key is undetermined from passover, set it.
                            if (databaseAction.primaryKey == null) {
                                databaseAction.setPrimaryKey(getPrimaryKeyFromPassover(
                                        databaseAction.itemType, databaseAction.idIdentifier,
                                        passoverIDs));
                            }

                            // This is the actual update item statement
                            Map<String, AttributeValueUpdate> updateItem = getUpdateItem(databaseAction, newlyCreatedID, passoverIDs);

                            // This is the updating marker statement
                            updateItem.put("marker", new AttributeValueUpdate(new AttributeValue().withN("1"), "ADD"));

                            Constants.debugLog("Update statement -------------------------");
                            for (Map.Entry<String, AttributeValueUpdate> entry : updateItem.entrySet()) {
                                if (entry.getValue().getAction().equals("DELETE")) {
                                    Constants.debugLog("Updating " + entry.getKey() + " with action " + entry.getValue()
                                            .getAction() + "!");
                                } else {
                                    Constants.debugLog("Updating " + entry.getKey() + " with action " + entry.getValue()
                                            .getAction() + " using value " + entry.getValue().getValue().toString() + "!");
                                }
                            }

                            Constants.debugLog("Using primary key = " + databaseAction.primaryKey.toString());
                            Constants.debugLog("On table = " + databaseAction.getTableName());

                            // This updates the object and the marker
                            transaction.updateItem(new UpdateItemRequest().withTableName(databaseAction.getTableName()).withKey
                                    (databaseAction.getKey()).withAttributeUpdates(updateItem));
                            uniqueActionsInCurrentTransaction++;

                            // This updates the marker
                            //transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                            //(databaseAction.item).withAttributeUpdates(markerUpdate));
                        } catch (Exception e) {
                            Constants.debugLog("ROLLING BACK TRANSACTION FROM ERROR = " + e.getLocalizedMessage());
                            transaction.rollback();
                            for (Transaction existingTransaction : transactions) {
                                existingTransaction.rollback();
                            }
                            throw new Exception("Error while trying to update an item in the database. Error: " + e
                                    .getLocalizedMessage(), e);
                        }
                        break;
                    case UPDATESAFE:
                        try {
                            // This is the key for getting the item
//                        Map<String, AttributeValue> key = new HashMap<>();
//                        key.put("item_type", databaseAction.item.get("item_type"));
//                        key.put("id", databaseAction.item.get("id"));

                            // This is the marker conditional statement
                            // String conditionalExpression = "#mark = :mark";
                            // Map<String, String> conditionalExpressionNames = new HashMap<>();
                            // conditionalExpressionNames.put("#mark", "marker");
                            // Map<String, AttributeValue> conditionalExpressionValues = new HashMap<>();

                            if (databaseAction.primaryKey == null) {
                                databaseAction.setPrimaryKey(getPrimaryKeyFromPassover(
                                        databaseAction.itemType, databaseAction.idIdentifier,
                                        passoverIDs));
                            }

                            // This is the actual update item statement
                            Map<String, AttributeValueUpdate> updateItem = getUpdateItem(databaseAction, newlyCreatedID, passoverIDs);

                            // This is the marker update statement
                            updateItem.put("marker", new AttributeValueUpdate(new AttributeValue().withN("1"), "ADD"));

                            Constants.debugLog("Update Safe statement -------------------------");
                            for (Map.Entry<String, AttributeValueUpdate> entry : updateItem.entrySet()) {
                                if (entry.getValue().getAction().equals("DELETE")) {
                                    Constants.debugLog("Updating " + entry.getKey() + " with action " + entry.getValue()
                                            .getAction() + "!");
                                } else {
                                    Constants.debugLog("Updating " + entry.getKey() + " with action " + entry.getValue()
                                            .getAction() + " using value " + entry.getValue().getValue().toString() + "!");
                                }
                            }

                            // We loop until we successfully update the item without the item being updated meanwhile
                            boolean ifFinished = false;
                            while (!ifFinished) {
                                // Read and check the expression
                                Constants.debugLog("Checking the viability of the item received");

                                // Use the checkHandler
                                String errorMessage = databaseAction.checkHandler.isViable(readItem(databaseAction.getTableName(), databaseAction.primaryKey));
                                if (errorMessage == null) {
                                    Constants.debugLog("The item is viable!!!");
                                    // Put the newly read marker value into the conditional statement
                                    // conditionalExpressionValues.put(":mark", new AttributeValue().withN(object.marker));

                                    try {
                                        // Try to update the item and the marker with the conditional check
                                        transaction.updateItem(new UpdateItemRequest().withTableName(databaseAction.getTableName())
                                                .withKey(databaseAction.getKey()).withAttributeUpdates(updateItem));
                                        uniqueActionsInCurrentTransaction++;

                                            /* TRANSACTIONS CONDITIONS NOT SUPPORTED
                                            .withConditionExpression(conditionalExpression).withExpressionAttributeNames
                                                    (conditionalExpressionNames).withExpressionAttributeValues(conditionalExpressionValues));
                                             */

                                        // Update the marker afterwards
                                        //transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                        //(databaseAction.item).withAttributeUpdates(markerUpdate));

                                        // This exits the loop
                                        ifFinished = true;
                                    } catch (ConditionalCheckFailedException ce) {
                                        // This means that the item was changed while we were trying to update it
                                        // Keep checking and trying again!
                                    }
                                } else {
                                    // If the object is no longer viable, then we need to throw an exception and exit
                                    Constants.debugLog("ROLLING BACK TRANSACTION FROM ERROR = " + errorMessage);
//                                    transaction.rollback();
                                    for (Transaction existingTransaction : transactions) {
                                        existingTransaction.rollback();
                                    }
                                    throw new Exception("The item failed the checkHandler: " + errorMessage);
                                }
                            }
                        } catch (Exception e) {
                            Constants.debugLog("ROLLING BACK TRANSACTION FROM ERROR = " + e.getLocalizedMessage());
                            transaction.rollback();
                            for (Transaction existingTransaction : transactions) {
                                existingTransaction.rollback();
                            }
                            throw new Exception("Error while trying to update an item in the database safely. Error: " +
                                    e.getLocalizedMessage(), e);
                        }
                        break;
                    case DELETE:
                        try {
                            // Delete the item
                            // If the primary key is undetermined from passover, set it.
                            if (databaseAction.primaryKey == null) {
                                databaseAction.setPrimaryKey(getPrimaryKeyFromPassover(
                                        databaseAction.itemType, databaseAction.idIdentifier,
                                        passoverIDs));
                            }
                            transaction.deleteItem(new DeleteItemRequest().withTableName(databaseAction.getTableName()).withKey
                                    (databaseAction.getKey()));
                            uniqueActionsInCurrentTransaction++;
                        } catch (Exception e) {
                            Constants.debugLog("ROLLING BACK TRANSACTION FROM ERROR = " + e.getLocalizedMessage());
                            transaction.rollback();
                            for (Transaction existingTransaction : transactions) {
                                existingTransaction.rollback();
                            }
                            throw new Exception("Error while deleting an item in the database. Error: " + e
                                    .getLocalizedMessage(), e);
                        }
                        break;
                    case DELETECONDITIONAL:
                        // Delete conditional essentially will use a checkHandler for the situation, but will only fail
                        // itself if it fails that checkHandler, it won't rollback the entire transaction.

                        // If the primary key is undetermined from passover, set it.
                        if (databaseAction.primaryKey == null) {
                            databaseAction.setPrimaryKey(getPrimaryKeyFromPassover(
                                    databaseAction.itemType, databaseAction.idIdentifier,
                                    passoverIDs));
                        }

                        try {
                            // This allows us to get the key
//                        Map<String, AttributeValue> key = new HashMap<>();
//                        key.put("item_type", databaseAction.item.get("item_type"));
//                        key.put("id", databaseAction.item.get("id"));

                            // This is the marker conditional statement
//                        String conditionalExpression = "#mark = :mark";
//                        Map<String, String> conditionalExpressionNames = new HashMap<>();
//                        conditionalExpressionNames.put("#mark", "marker");
//                        Map<String, AttributeValue> conditionalExpressionValues = new HashMap<>();

                            boolean ifFinished = false;
                            while (!ifFinished) {
                                DatabaseItem databaseItem = readItem(databaseAction.getTableName(), databaseAction.primaryKey);
                                // Perform the checkHandler check
                                String errorMessage = databaseAction.checkHandler.isViable(databaseItem);
                                if (errorMessage == null) {
//                                conditionalExpressionValues.put(":mark", new AttributeValue().withN(object.marker));

                                    try {
                                        // try to delete the item
                                        transaction.deleteItem(new DeleteItemRequest().withTableName(databaseAction.getTableName())
                                                .withKey(databaseAction.getKey()));
                                        uniqueActionsInCurrentTransaction++;

                                            /* CONDITIONS NOT SUPPORTED
                                            .withConditionExpression(conditionalExpression)
                                            .withExpressionAttributeNames(conditionalExpressionNames)
                                            .withExpressionAttributeValues(conditionalExpressionValues));
                                            */

                                        ifFinished = true;
                                        // the process was successful.
                                    }
                                    // If the marker conditional statement fails, repeat
                                    catch (ConditionalCheckFailedException ce) {
                                        // This means that the item was changed and we need to try it again
                                    }
                                } else {
                                    // This means that the process was not successful, but because this is a delete
                                    // conditional, we will simply just keep going, as this means that the item no longer
                                    // needs to be deleted.
                                    Constants.debugLog("Couldn't delete from condition: " + errorMessage);
                                    ifFinished = true;
                                }
                            }
                        } catch (Exception e) {
                            // Else if there is another failure, rollback everything
                            Constants.debugLog("ROLLING BACK TRANSACTION FROM ERROR = " + e.getLocalizedMessage());
                            transaction.rollback();
                            for (Transaction existingTransaction : transactions) {
                                existingTransaction.rollback();
                            }
                            throw new Exception("Error while trying to conditionally delete an item. Error: " + e
                                    .getLocalizedMessage(), e);
                        }
                        break;
                }

                // If we are over the limit, then we do the transaction and keep going
                if (uniqueActionsInCurrentTransaction >= transactionActionLimit) {
                    try {
                        transaction.commit();
                        transaction.delete();
                    }
                    catch (TransactionRolledBackException | UnknownCompletedTransactionException e) {
                        Constants.debugLog("Error occurred, making the database a little worse.... Error: "
                                + e.getLocalizedMessage());
                    }
                    transaction = txManager.newTransaction();
                    uniqueActionsInCurrentTransaction = 0;
                }
            }

            // If it gets here, then the process completed safely.
//            transaction.commit();
////            transactions.add(transaction);
//            transaction.delete();
            try {
                transaction.commit();
                transaction.delete();
            }
            catch (TransactionRolledBackException | UnknownCompletedTransactionException e) {
                Constants.debugLog("Error occurred, making the database a little worse.... Error: "
                    + e.getLocalizedMessage());
            }

            SingletonTimer.get().endAndPushCheckpoint("Sending the notifications");

            // Then it is safe to use Ably to send the notifications
            databaseActionCompiler.sendNotifications();
        }

        SingletonTimer.get().endCheckpoint();

        // This is the most dangerous part of the code, where things could actually go really wrong
        // Commit and delete all the transactions at the end.
//        List<Exception> errors = new ArrayList<>();
//        for (Transaction transaction : transactions) {
//            try {
//                transaction.commit();
//                transaction.delete();
//            }
//            catch (TransactionRolledBackException | UnknownCompletedTransactionException e) {
//                errors.add(e);
//                Constants.debugLog("Error occurred, making the database a little worse.... Error: "
//                    + e.getLocalizedMessage());
//            }
//        }
//        if (errors.size() > 0) {
//            throw errors.get(0);
//        }


        return returnString;
    }

    /**
     * TODO
     *
     * @param itemType
     * @param idIdentifier
     * @param passoverIDs
     * @return
     * @throws Exception
     */
    private PrimaryKey getPrimaryKeyFromPassover(String itemType, String idIdentifier, Map<String, String> passoverIDs) throws Exception {
        if (itemType.equals("Message")) {
            throw new Exception("Cannot get primary key for Message from passover yet");
        }
        if (!passoverIDs.containsKey(idIdentifier)) {
            throw new Exception("Passover Identifier not recognized for update action. " +
                    "identifier = " + idIdentifier);
        }
        return new PrimaryKey("item_type", itemType, "id", passoverIDs.get(idIdentifier));
    }

    /**
     * Gets the Put Item, accounting for any passover that may occur in the process. (Passover meaning
     * if there are multiple transactions and an item wants the newly created ID of the previous
     * created item.)
     *
     * @param databaseAction The CREATE database action.
     * @param passoverID The potential ID from the previous created item.
     * @param passoverIDs The map of passoverIdentifiers to passoverIDs for the current transaction.
     * @return The Map that indicates the Put Item.
     * @throws Exception If the statement is malformed or done in the wrong order.
     */
    private Map<String, AttributeValue> getPutItem(DatabaseAction databaseAction,
                                                            String passoverID, Map<String, String>
                                                           passoverIDs) throws Exception {
        if (databaseAction.ifWithCreate) {
            if (passoverID == null) {
                throw new Exception("An ifWithCreate CREATE statement must be after an initial CREATE statement!");
            }
            else {
                for (Map.Entry<String, AttributeValue> entry : databaseAction.item.entrySet()) {
                    String name = entry.getKey();
                    AttributeValue value = entry.getValue();
                    if (value.getS() != null && value.getS().equals("")) {
                        if (databaseAction.passoverIdentifiers.containsKey(name)) {
                            // This means that the database action has specifically specified
                            // this ID to be used for this passover ID.
                            value.setS(passoverIDs.get(databaseAction.passoverIdentifiers.
                                    get(name)));
                        }
                        else {
                            value.setS(passoverID);
                        }
                    }
                }
            }
        }
        return databaseAction.item;
    }

    /**
     * Updates the database action to include all the newly created IDs from the CREATE statement.
     * Specifically replaces all the "" values with the new ID.
     *
     * TODO This only looks inside String values, not String sets. Is that a problem?
     *
     * @param databaseAction The {@link DatabaseAction} to update using the ID.
     * @param newID The newly generated ID to fill all the action's fields with.
     * @param passoverIDs The map of passoverIdentifiers to passoverIDs for the current transaction.
     * @return The new {@link AttributeValueUpdate} map to use in the update statement.
     * @throws Exception If the update is in the wrong order (and cannot update with the newID).
     */
    private Map<String, AttributeValueUpdate> getUpdateItem(DatabaseAction databaseAction,
                                                            String newID,
                                                            Map<String, String> passoverIDs)
                                                            throws Exception {
        if (databaseAction.ifWithCreate) {
            if (newID == null) {
                throw new Exception("The Create statement needs to be first while updating!");
            }
            else {
                List<String> stringList = new ArrayList<>();
                stringList.add(newID);
                for (Map.Entry<String, AttributeValueUpdate> entry: databaseAction.updateItem.entrySet()) {
                    String attributeName = entry.getKey();
                    AttributeValueUpdate valueUpdate = entry.getValue();
                    // For each one that uses the id, switch it to the return string
                    if (valueUpdate.getValue().getS() != null && valueUpdate.getValue().getS().equals("")) {
                        if (databaseAction.passoverIdentifiers.containsKey(attributeName)) {
                            // This means that the database action has specifically specified
                            // this ID to be used for this passover ID.
                            String passoverID = passoverIDs.get(databaseAction.
                                    passoverIdentifiers.get(attributeName));
                            valueUpdate.setValue(new AttributeValue(passoverID));
                        }
                        else {
                            // There are no passoverIDs claiming it, therefore it is purely a in-
                            // compiler created ID.
                            if (valueUpdate.getAction().equals("PUT")) {
                                valueUpdate.setValue(new AttributeValue(newID));
                            }
                            else if (valueUpdate.getAction().equals("ADD") || valueUpdate.getAction()
                                .equals("DELETE")) {
                                valueUpdate.setValue(new AttributeValue(stringList));
                            }
                        }
                    }
                }

                return databaseAction.updateItem;
            }
        }
        else {
            return databaseAction.updateItem;
        }
    }

    /**
     * Calculates a new ID for an item based on the item type, then checks to see if it already
     * exists or not. It continues to try until it finds a free ID, in which case it returns the new
     * ID.
     *
     * @param itemType The String type of the item to create the new ID for.
     * @return The newly generated and unique ID for the item.
     */
    private String getNewID(String itemType) {
        String id = null;
        boolean ifFound = false;
        while (!ifFound) {
            id = generateRandomID(itemType);
            if (databaseTable.getItem("item_type", itemType, "id", id) == null) {
                ifFound = true;
            }
        }
        return id;
    }

    /**
     * Generates a random String ID based on the item's type. Takes a number of letters from the
     * type and makes them into a prefix. Then adds a number of random integers onto the end.
     *
     * @param itemType The String type of the item to create an ID for.
     * @return The randomly generated ID for the item.
     */
    static private String generateRandomID(String itemType) {
        String prefix = itemType.substring(0, Constants.numPrefix).toUpperCase();
        int numDigits = Constants.idLength - Constants.numPrefix;

        Random random = new Random();
        double range = random.nextDouble();
        String idNum = Long.toString((long)(range*Math.pow(10, numDigits)));
        StringBuilder idBuilder = new StringBuilder(idNum);

        while (idNum.length() != numDigits) {
            idBuilder.insert(0, "0");
        }

        idBuilder.insert(0, prefix);
        return idBuilder.toString();
    }

    /**
     * Reads an item from the database, using the table name and the primary key of the item (so
     * that we can read from any table, no matter what the partition and sort key are). Uses the
     * {@link DatabaseItemFactory} in order to instantiate the item into a valid Java object.
     *
     * @param tableName The name of the table to read from.
     * @param primaryKey The primary key of the item to read from the table.
     * @return The {@link DatabaseItem} of the item that matches the description given.
     * @throws Exception If the item was not found or the factory does not recognize the item type.
     */
    public DatabaseItem readItem(String tableName, PrimaryKey primaryKey) throws Exception {
        Item item = tables.get(tableName).getItem(primaryKey);
        Constants.debugLog("Reading item with PrimaryKey = " + primaryKey.toString());

        // Check is the item didn't return anything
        if (item == null) {
            throw new ItemNotFoundException("No item in the database with PrimaryKey = " + primaryKey.toString());
        }
        return DatabaseItemFactory.build(item);
    }


    /**
     * Checks to see if the inputted username already exists inside of the database, in order to
     * catch errors earlier.
     *
     * @param username The String username to check.
     * @param item_type The String item type of the item to hold the username.
     * @return Whether or not the username is inside the database already.
     */
    private boolean usernameInDatabase(String username, String item_type) {
        Index index = databaseTable.getIndex("item_type-username-index");
        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#usr", "username");
        nameMap.put("#type", "item_type");
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":usr", username);
        valueMap.put(":type", item_type);
        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#type = :type AND #usr = :usr")
                .withNameMap(nameMap).withValueMap(valueMap);
        index.query(querySpec);
        ItemCollection<QueryOutcome> items = index.query(querySpec);
        Iterator<Item> iterator = items.iterator();
        return (iterator.hasNext());
    }

    /**
     * Converts from a map of {@link AttributeValue} object to regular Java {@link Object}. Used for
     * initializing the notifications for real time updates in the notification handler.
     */
    private Map<String, Object> convertFromAttributeValueMap(Map<String, AttributeValue> map) {
        Map<String, Object> returnMap = new HashMap<>();
        for (Map.Entry<String, AttributeValue> entry : map.entrySet()) {
            String name = entry.getKey();
            AttributeValue value = entry.getValue();
            if (value != null) {
                if (value.getS() != null) {
                    returnMap.put(name, value.getS());
                } else if (value.getSS() != null) {
                    returnMap.put(name, value.getSS());
                } else if (value.getN() != null) {
                    returnMap.put(name, value.getN());
                }
            }
        }
        return returnMap;
    }
}
