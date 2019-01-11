package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
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
//        tables.put(Constants.databaseTableName, new Table(client, Constants.databaseTableName));
//        tables.put(Constants.messageTableName, new Table(client, Constants.messageTableName));
    }

    /*
    For SAFE operations, we don't necessarily want to have to have to read the marker beforehand.
    So we first read the object and then we use the checkHandler to make sure that it's okay to do.
    Then we check to see if it is changed while we are trying to do stuff to it.
     */

    // Returns the ID if it's applicable
    public String attemptTransaction(DatabaseActionCompiler databaseActionCompiler) throws Exception {
        // Marker retry implemented
        Transaction transaction = txManager.newTransaction();
        String returnString = null;

        List<DatabaseAction> databaseActions = databaseActionCompiler.getDatabaseActions();

        for (DatabaseAction databaseAction : databaseActions) {
            switch (databaseAction.action) {
                case CREATE:
                    try {
                        // Get the ID!
                        String id = getNewID(databaseAction.itemType);
                        databaseAction.item.put("id", new AttributeValue(id));
                        databaseAction.item.put("time_created", new AttributeValue(new DateTime().toString()));
                        ((CreateDatabaseAction)databaseAction).updateWithIDHandler.updateWithID(databaseAction.item, id);

                        if (databaseAction.item.containsKey("username")) {
                            if (this.usernameInDatabase(databaseAction.item.get("username").getS(), databaseAction.item
                                    .get("item_type").getS())) {
                                // This means that the database already has this username, we should not do this
                                throw new Exception("Attempted to put repeat username: " + databaseAction.item.get
                                        ("username").getS() + " into the database!");
                            }
                        }

                        Constants.debugLog("Creating item with item: " + databaseAction.item);

                        transaction.putItem(new PutItemRequest().withTableName(databaseAction.getTableName()).withItem
                                (databaseAction.item));
                        returnString = id;
                    }
                    catch (Exception e) {
                        transaction.rollback();
                        throw new Exception("Error while trying to create item in the database! Error: " + e
                                .getLocalizedMessage(), e);
                    }
                    break;
                case UPDATE:
                    try {
                        // This is the actual update item statement
                        Map<String, AttributeValueUpdate> updateItem = getUpdateItem(databaseAction, returnString);

                        // This is the updating marker statement
                        updateItem.put("marker", new AttributeValueUpdate(new AttributeValue().withN("1"), "ADD"));

                        Constants.debugLog("Update statement -------------------------");
                        for (Map.Entry<String, AttributeValueUpdate> entry : updateItem.entrySet()) {
                            if (entry.getValue().getAction().equals("DELETE")) {
                                Constants.debugLog("Updating " + entry.getKey() + " with action " + entry.getValue()
                                        .getAction() + "!");
                            }
                            else {
                                Constants.debugLog("Updating " + entry.getKey() + " with action " + entry.getValue()
                                        .getAction() + " using value " + entry.getValue().getValue().toString() + "!");
                            }
                        }

                        // This updates the object and the marker
                        transaction.updateItem(new UpdateItemRequest().withTableName(databaseAction.getTableName()).withKey
                                (databaseAction.getKey()).withAttributeUpdates(updateItem));

                        // This updates the marker
                        //transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                //(databaseAction.item).withAttributeUpdates(markerUpdate));
                    }
                    catch (Exception e) {
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
                        Map<String, AttributeValueUpdate> updateItem = getUpdateItem(databaseAction, returnString);

                        // This is the marker update statement
                        updateItem.put("marker", new AttributeValueUpdate(new AttributeValue().withN("1"), "ADD"));

                        Constants.debugLog("Update Safe statement -------------------------");
                        for (Map.Entry<String, AttributeValueUpdate> entry : updateItem.entrySet()) {
                            if (entry.getValue().getAction().equals("DELETE")) {
                                Constants.debugLog("Updating " + entry.getKey() + " with action " + entry.getValue()
                                        .getAction() + "!");
                            }
                            else {
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
                            }
                            else {
                                // If the object is no longer viable, then we need to throw an exception and exit
                                throw new Exception("The item failed the checkHandler: " + errorMessage);
                            }
                        }
                    }
                    catch (Exception e) {
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
                    }
                    catch (Exception e) {
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
                            }
                            else {
                                // This means that the process was not successful, but because this is a delete
                                // conditional, we will simply just keep going, as this means that the item no longer
                                // needs to be deleted.
                                Constants.debugLog("Couldn't delete from condition: " + errorMessage);
                                ifFinished = true;
                            }
                        }
                    }
                    catch (Exception e) {
                        // Else if there is another failure, rollback everything
                        transaction.rollback();
                        throw new Exception("Error while trying to conditionally delete an item. Error: " + e
                                .getLocalizedMessage(), e);
                    }
                    break;
            }
        }

        // If it gets here, then the process completed safely.
        transaction.commit();
        transaction.delete();

        // Then it is safe to use Ably to send the notifications
        databaseActionCompiler.sendNotifications();

        return returnString;
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
            throw new Exception("No item in the database with PrimaryKey = " + primaryKey.toString());
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
