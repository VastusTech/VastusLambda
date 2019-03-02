package main.java.databaseOperations;

import main.java.Logic.Constants;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.TransactionManager;
import main.java.databaseObjects.*;
import main.java.databaseOperations.exceptions.ItemNotFoundException;

import org.joda.time.DateTime;

import java.util.*;

// TODO ALl of these classes should throw specific exceptions (Maybe that we define?)
public class DynamoDBHandler {
    // Singleton Pattern!
    private static DynamoDBHandler instance;

    private TransactionManager txManager;
    public AmazonDynamoDB client;
    private Map<String, Table> tables;
    private Table databaseTable;
    private Table messageTable;
    private Table firebaseTokenTable;

    // Used for the singleton pattern!
    static public DynamoDBHandler getInstance() throws Exception {
        if (DynamoDBHandler.instance == null) {
            DynamoDBHandler.instance = new DynamoDBHandler();
        }
        return DynamoDBHandler.instance;
    }

    private DynamoDBHandler() throws Exception {
        // AWS CREDENTIALS GO HERE
        client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

        long waitTimeSeconds = 10 * 60;

        TransactionManager.verifyOrCreateTransactionTable(client, "Transactions", 10, 10, waitTimeSeconds);
        TransactionManager.verifyOrCreateTransactionImagesTable(client, "TransactionImages", 10, 10, waitTimeSeconds);

        txManager = new TransactionManager(client, "Transactions", "TransactionImages");

        tables = new HashMap<>();
        databaseTable = new Table(client, Constants.databaseTableName);
        messageTable = new Table(client, Constants.messageTableName);
        firebaseTokenTable = new Table(client, Constants.firebaseTokenTableName);
        tables.put(Constants.databaseTableName, databaseTable);
        tables.put(Constants.messageTableName, messageTable);
    }

    /*
    For SAFE operations, we don't necessarily want to have to have to read the marker beforehand.
    So we first read the object and then we use the checkHandler to make sure that it's okay to do.
    Then we check to see if it is changed while we are trying to do stuff to it.
     */

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
        // TODO ==============================================================================
        // TODO BECAUSE TRANSACTIONS ARE BROKEN UP, LET'S DO ALL THE CHECKING BEFOREHAND INSTEAD
        // TODO IN ORDER TO INCREASE THE CHANCE OF AN "ATOMIC" FUNCTIONALITY!!!!!!
        // TODO ==============================================================================
        final int transactionActionLimit = 10; // Transactions can only handle 10 unique things each
        // This indicates created ID for current compiler and also supports passover between transactions.
        String newlyCreatedID = null; // This indicates the created ID for the current compiler.
        String returnString = null; // This indicates the created ID for the first compiler

        // Pre-checking
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
                            databaseAction.item.put("time_created", new AttributeValue(new DateTime().toString()));
                            ((CreateDatabaseAction) databaseAction).updateWithIDHandler.updateWithID(databaseAction.item, id);

                            if (databaseAction.item.containsKey("username")) {
                                if (this.usernameInDatabase(databaseAction.item.get("username").getS(), databaseAction.item
                                        .get("item_type").getS())) {
                                    // This means that the database already has this username, we should not do this
                                    throw new Exception("Attempted to put repeat username: " + databaseAction.item.get
                                            ("username").getS() + " into the database!");
                                }
                            }

                            // TODO THIS WORKS FOR ONLY IF IT'S a STRING VALUE, NOT INSIDE A STRING SET, if that's a problem, reevaluate...
                            Map<String, AttributeValue> putItem = getPutItem(databaseAction, newlyCreatedID);

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
                            transaction.rollback();
                            throw new Exception("Error while trying to create item in the database! Error: " + e
                                    .getLocalizedMessage(), e);
                        }
                        break;
                    case UPDATE:
                        try {
                            // This is the actual update item statement
                            Map<String, AttributeValueUpdate> updateItem = getUpdateItem(databaseAction, newlyCreatedID);

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

                            // This updates the object and the marker
                            transaction.updateItem(new UpdateItemRequest().withTableName(databaseAction.getTableName()).withKey
                                    (databaseAction.getKey()).withAttributeUpdates(updateItem));
                            uniqueActionsInCurrentTransaction++;

                            // This updates the marker
                            //transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                            //(databaseAction.item).withAttributeUpdates(markerUpdate));
                        } catch (Exception e) {
                            transaction.rollback();
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

                            // This is the actual update item statement
                            Map<String, AttributeValueUpdate> updateItem = getUpdateItem(databaseAction, newlyCreatedID);

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
                                DatabaseItem databaseItem = readItem(databaseAction.getTableName(), databaseAction.primaryKey);

                                // Use the checkHandler
                                Constants.debugLog("Checking the viability of the item received");
                                String errorMessage = databaseAction.checkHandler.isViable(databaseItem);
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
                                    throw new Exception("The item failed the checkHandler: " + errorMessage);
                                }
                            }
                        } catch (Exception e) {
                            transaction.rollback();
                            throw new Exception("Error while trying to update an item in the database safely. Error: " +
                                    e.getLocalizedMessage(), e);
                        }
                        break;
                    case DELETE:
                        try {
                            // Delete the item
                            transaction.deleteItem(new DeleteItemRequest().withTableName(databaseAction.getTableName()).withKey
                                    (databaseAction.getKey()));
                            uniqueActionsInCurrentTransaction++;
                        } catch (Exception e) {
                            transaction.rollback();
                            throw new Exception("Error while deleting an item in the database. Error: " + e
                                    .getLocalizedMessage(), e);
                        }
                        break;
                    case DELETECONDITIONAL:
                        // Delete conditional essentially will use a checkHandler for the situation, but will only fail
                        // itself if it fails that checkHandler, it won't rollback the entire transaction.

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
                            transaction.rollback();
                            throw new Exception("Error while trying to conditionally delete an item. Error: " + e
                                    .getLocalizedMessage(), e);
                        }
                        break;
                }

                // If we are over the limit, then we do the transaction and keep going
                if (uniqueActionsInCurrentTransaction >= transactionActionLimit) {
                    transaction.commit();
                    transaction.delete();
                    transaction = txManager.newTransaction();
                    uniqueActionsInCurrentTransaction = 0;
                }
            }

            // If it gets here, then the process completed safely.
            transaction.commit();
            transaction.delete();

            // Then it is safe to use Ably to send the notifications
            databaseActionCompiler.sendNotifications();
        }

        return returnString;
    }

    /**
     * Gets the Put Item, accounting for any passover that may occur in the process. (Passover meaning
     * if there are multiple transactions and an item wants the newly created ID of the previous
     * created item.)
     *
     * @param databaseAction The CREATE database action.
     * @param passoverID The potential ID from the previous created item.
     * @return The Map that indicates the Put Item.
     * @throws Exception If the statement is malformed or done in the wrong order.
     */
    private Map<String, AttributeValue> getPutItem(DatabaseAction databaseAction,
                                                            String passoverID) throws Exception {
        if (databaseAction.ifWithCreate) {
            if (passoverID == null) {
                throw new Exception("An ifWithCreate CREATE statement must be after an initial CREATE statement!");
            }
            else {
                for (AttributeValue value : databaseAction.item.values()) {
                    if (value.getS() != null && value.getS().equals("")) {
                        value.setS(passoverID);
                    }
                }
            }
        }
        return databaseAction.item;
    }

    private Map<String, AttributeValueUpdate> getUpdateItem(DatabaseAction databaseAction,
                                                            String returnString) throws Exception {
        if (databaseAction.ifWithCreate) {
            if (returnString == null) {
                throw new Exception("The Create statement needs to be first while updating!");
            }
            else {
                List<String> stringList = new ArrayList<>();
                stringList.add(returnString);
                for (AttributeValueUpdate valueUpdate: databaseAction.updateItem.values()) {
                    // For each one that uses the id, switch it to the return string
                    if (valueUpdate.getAction().equals("PUT")) {
                        if (valueUpdate.getValue().getS() != null && valueUpdate.getValue().getS().equals("")) {
                            valueUpdate.setValue(new AttributeValue(returnString));
                        }
                    }
                    else if (valueUpdate.getAction().equals("ADD") || valueUpdate.getAction().equals("DELETE")) {
                        if (valueUpdate.getValue().getS() != null && valueUpdate.getValue().getS().equals("")) {
                            valueUpdate.setValue(new AttributeValue(stringList));
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

    public String getNewID(String itemType) throws Exception {
        String id = null;
        boolean ifFound = false;
        while (!ifFound) {
            id = generateRandomID(itemType);
            // TODO Does it just return null if not found?
            if (databaseTable.getItem("item_type", itemType, "id", id) == null) {
                ifFound = true;
            }
        }
        return id;
    }

    static public String generateRandomID(String itemType) {
        String prefix = itemType.substring(0, Constants.numPrefix).toUpperCase();
        int numDigits = Constants.idLength - Constants.numPrefix;

        Random random = new Random();
        double range = random.nextDouble();
        String idNum = Long.toString((long)(range*Math.pow(10, numDigits)));

        while (idNum.length() != numDigits) {
            idNum = "0" + idNum;
        }

        return prefix + idNum;
    }

    public DatabaseItem readItem(String tableName, PrimaryKey primaryKey) throws Exception {
        Item item = tables.get(tableName).getItem(primaryKey);
        Constants.debugLog("Reading item with PrimaryKey = " + primaryKey.toString());

        // Check is the item didn't return anything
        if (item == null) {
            throw new ItemNotFoundException("No item in the database with PrimaryKey = " + primaryKey.toString());
        }
        return DatabaseItemBuilder.build(item);
    }

    // TODO Way to differentiate between no user showed up and error?
    // TODO YES by throwing an exception!
//    public <T extends DatabaseObject> T usernameQuery(String username, String itemType) throws Exception {
//        Index index = databaseTable.getIndex("item_type-username-index");
//
//        HashMap<String, String> nameMap = new HashMap<>();
//        nameMap.put("#usr", "username");
//        nameMap.put("#type", "item_type");
//
//        HashMap<String, Object> valueMap = new HashMap<>();
//        valueMap.put(":usr", username);
//        valueMap.put(":type", itemType);
//
//        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#type = :type AND #usr = :usr")
//                .withNameMap(nameMap).withValueMap(valueMap);
//
//        Item resultItem = null;
//
//        try {
//            ItemCollection<QueryOutcome> items = index.query(querySpec);
//            Iterator<Item> iterator = items.iterator();
//            int i = 0;
//            if (!iterator.hasNext()) {
//                // This means that nothing showed up in the query,
//                return null;
//            }
//            while (iterator.hasNext()) {
//                if (i > 0) {
//                    // This means that the query came up with more than one result
//                    throw new Exception("More than one item has " + username + " as their username!!!!");
//                }
//                resultItem = iterator.next();
//                i++;
//            }
//        }
//        catch (Exception e) {
//            throw new Exception("Error while username querying. Error: " + e.getLocalizedMessage());
//        }
//
//        if (resultItem != null) {
//            return (T)DatabaseItemBuilder.build(resultItem);
//        }
//
//        return null;
//    }

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

    public Set<String> getFirebaseTokens(String userID) {
        Item item = firebaseTokenTable.getItem("id", userID);
        Set<String> tokens = null;
        if (item != null) {
            tokens = item.getStringSet("tokens");
        }

        // String expires = item.getString("expires");
        // TODO Make sure that these tokens haven't expired?

        if (tokens == null) {
            return new HashSet<>();
        }
        else {
            return tokens;
        }
    }

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

    // TODO We usually don't want to send out a null variable without throwing an exception
//    public <T extends DatabaseObject> List<T> getAll(String itemType) throws Exception {
//        HashMap<String, String> nameMap = new HashMap<>();
//        nameMap.put("#type", "item_type");
//
//        HashMap<String, Object> valueMap = new HashMap<>();
//        valueMap.put(":type", itemType);
//
//        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#type = :type").withNameMap(nameMap)
//                .withValueMap(valueMap);
//
//        ItemCollection<QueryOutcome> items = databaseTable.query(querySpec);
//        Iterator<Item> iterator = items.iterator();
//        List<T> allList = new ArrayList<>();
//        while (iterator.hasNext()) {
//            Item item = iterator.next();
//            allList.add((T)DatabaseItemBuilder.build(item));
//        }
//
//        return allList;
//    }
}
