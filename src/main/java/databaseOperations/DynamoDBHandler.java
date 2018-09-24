package main.java.databaseOperations;

import main.java.Logic.Constants;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.TransactionManager;
import main.java.databaseObjects.*;

import java.util.*;

// TODO ALl of these classes should throw specific exceptions (Maybe that we define?)
public class DynamoDBHandler {
    // Singleton Pattern!
    private static DynamoDBHandler instance;

    String tableName;
    TransactionManager txManager;
    AmazonDynamoDB client;
    Table table;

    // Used for the singleton pattern!
    static public DynamoDBHandler getInstance() throws Exception {
        if (DynamoDBHandler.instance == null) {
            DynamoDBHandler.instance = new DynamoDBHandler();
        }
        return DynamoDBHandler.instance;
    }

    private DynamoDBHandler() throws Exception {
        this.tableName = Constants.databaseTableName;
        // TODO AWS CREDENTIALS GO HERE?
        client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

        long waitTimeSeconds = 10 * 60;

        TransactionManager.verifyOrCreateTransactionTable(client, "Transactions", 10, 10, waitTimeSeconds);
        TransactionManager.verifyOrCreateTransactionImagesTable(client, "TransactionImages", 10, 10, waitTimeSeconds);

        txManager = new TransactionManager(client, "Transactions", "TransactionImages");

        table = new Table(client, tableName);
    }

    /*
    For SAFE operations, we don't necessarily want to have to have to read the marker beforehand.
    So we first read the object and then we use the checkHandler to make sure that it's okay to do.
    Then we check to see if it is changed while we are trying to do stuff to it.
     */

    // Returns the ID if it's applicable
    // TODO Make sure that there are no more than 1 create action in the list
    public String attemptTransaction(List<DatabaseAction> databaseActions) throws Exception {
        // Marker retry implemented
        Transaction transaction = txManager.newTransaction();
        String returnString = null;

        for (DatabaseAction databaseAction : databaseActions) {
            switch (databaseAction.action) {
                case CREATE:
                    try {
                        // Get the ID!
                        String id = getNewID(databaseAction.item.get("item_type").getS());
                        databaseAction.item.put("id", new AttributeValue(id));
                        transaction.putItem(new PutItemRequest().withTableName(tableName).withItem(databaseAction.item));
                        returnString = id;
                    }
                    catch (Exception e) {
                        transaction.rollback();
                        throw new Exception("Error while trying to create item in the database! Error: " + e
                                .getLocalizedMessage());
                    }
                    break;
                case UPDATE:
                    try {
                        Map<String, AttributeValueUpdate> updateItem = new HashMap<>();
                        if (databaseAction.ifWithCreate) {
                            if (returnString == null) {
                                throw new Exception("The Create statement needs to be first while updating!");
                            }
                            else {
                                updateItem.put(databaseAction.updateAttributeName, new AttributeValueUpdate(new
                                        AttributeValue(returnString), databaseAction.updateAction));
                            }
                        }
                        else {
                            updateItem.put(databaseAction.updateAttributeName, new AttributeValueUpdate
                                    (databaseAction.updateAttribute, databaseAction.updateAction));
                        }

                        transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                (databaseAction.item).withAttributeUpdates(updateItem));

                        Map<String, AttributeValueUpdate> markerUpdate = new HashMap<>();
                        markerUpdate.put("marker", new AttributeValueUpdate(new AttributeValue().withN("1"), "ADD"));
                        transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                (databaseAction.item).withAttributeUpdates(markerUpdate));
                    }
                    catch (Exception e) {
                        transaction.rollback();
                        throw new Exception("Error while trying to update an item in the database. Error: " + e
                                .getLocalizedMessage());
                    }
                    break;
                case UPDATESAFE:
                    try {
                        boolean ifFinished = false;
                        // TODO Check error?
                        String id = databaseAction.item.get("id").getS();
                        String itemType = databaseAction.item.get("item_type").getS();
                        Map<String, AttributeValue> key = new HashMap<>();
                        key.put("item_type", new AttributeValue(itemType));
                        key.put("id", new AttributeValue(id));

                        String conditionalExpression = "#mark = :mark";
                        Map<String, String> conditionalExpressionNames = new HashMap<>();
                        conditionalExpressionNames.put("#mark", "marker");
                        Map<String, AttributeValue> conditionalExpressionValues = new HashMap<>();

                        Map<String, AttributeValueUpdate> updateItem = new HashMap<>();
                        if (databaseAction.ifWithCreate) {
                            if (returnString == null) {
                                throw new Exception("The Create statement needs to be first while updating!");
                            }
                            else {
                                updateItem.put(databaseAction.updateAttributeName, new AttributeValueUpdate(new
                                        AttributeValue(returnString), databaseAction.updateAction));
                            }
                        }
                        else {
                            updateItem.put(databaseAction.updateAttributeName, new AttributeValueUpdate
                                    (databaseAction.updateAttribute, databaseAction.updateAction));
                        }

                        while (!ifFinished) {
                            // Read and check the expression
                            DatabaseObject object = readItem(key);

                            // Use the checkHandler
                            if (databaseAction.checkHandler.isViable(object)) {
                                conditionalExpressionValues.put(":mark", new AttributeValue().withN(object.marker));

                                try {
                                    transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                            (databaseAction.item).withAttributeUpdates(updateItem)
                                            .withConditionExpression(conditionalExpression).withExpressionAttributeNames
                                                    (conditionalExpressionNames).withExpressionAttributeValues(conditionalExpressionValues));

                                    Map<String, AttributeValueUpdate> markerUpdate = new HashMap<>();
                                    markerUpdate.put("marker", new AttributeValueUpdate(new AttributeValue().withN("1"), "ADD"));
                                    transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                            (databaseAction.item).withAttributeUpdates(markerUpdate));
                                    ifFinished = true;
                                } catch (ConditionalCheckFailedException ce) {
                                    // Keep checking and trying again!
                                    // TODO I might not need this catch because of the lower one?
                                } catch (Exception e) {
                                    transaction.rollback();
                                    throw new Exception("Error while trying to update an item in the database");
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        transaction.rollback();
                        throw new Exception("Error while trying to update an item in the database safely. Error: " +
                                e.getLocalizedMessage());
                    }
                    break;
                case DELETE:
                    try {
                        transaction.deleteItem(new DeleteItemRequest().withTableName(tableName).withKey
                                (databaseAction.item));
                    }
                    catch (Exception e) {
                        transaction.rollback();
                        throw new Exception("Error while deleting an item in the database. Error: " + e
                                .getLocalizedMessage());
                    }
                    break;
                case DELETESAFE:
                    // TODO IS THERE A REASON TO ACTUALLY IMPLEMENT THIS?
//                    try {
//                        transaction.deleteItem(new DeleteItemRequest().withTableName(tableName).withKey
//                                (databaseAction.item).withConditionExpression(databaseAction.conditionalExpression));
//                    }
//                    catch (ConditionalCheckFailedException ce) {
//                        // TODO Implement this!
//                        // This means that the app needs to try again
//                    }
//                    catch (Exception e) {
//                        transaction.rollback();
//                        throw new Exception("Error while deleting an item in the database");
//                    }
                    break;
            }
        }

        // If it gets here, then the process completed safely.
        transaction.commit();
        transaction.delete();
        return returnString;
    }

    public String getNewID(String itemType) throws Exception {
        String id = null;
        boolean ifFound = false;
        while (!ifFound) {
            id = generateRandomID(itemType);
            // TODO Does it just return null if not found?
            if (table.getItem("item_type", itemType, "id", id) == null) {
                ifFound = true;
            }
        }
        return id;
    }

    static public String generateRandomID(String itemType) {
        String prefix = itemType.substring(0, Constants.numPrefix);
        int numDigits = Constants.idLength - Constants.numPrefix;

        Random random = new Random();
        String idNum = Integer.toString(random.nextInt((int)Math.pow(10, numDigits)));

        while (idNum.length() != numDigits) {
            idNum = "0" + idNum;
        }

        return prefix + idNum;
    }

    public <T extends DatabaseObject> T readItem(Map<String, AttributeValue> key) throws Exception {
        try {
            String id = key.get("id").getS();
            String itemType = key.get("item_type").getS();
            Item item = table.getItem("item_type", itemType, "id", id);
            return (T)DatabaseObjectBuilder.build((Map<String, AttributeValue>)item);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO Way to differentiate between no user showed up and error?
    // TODO YES by throwing an exception!
    public <T extends DatabaseObject> T usernameQuery(String username, String itemType) throws Exception {
        Index index = table.getIndex("item_type-username-index");

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#usr", "username");
        nameMap.put("#type", "item_type");

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":usr", username);
        valueMap.put(":type", itemType);

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#type = :type AND #usr = :usr")
                .withNameMap(nameMap).withValueMap(valueMap);

        try {
            ItemCollection<QueryOutcome> items = index.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            int i = 0;
            if (!iterator.hasNext()) {
                // This means that nothing showed up in the query,

            }
            while (iterator.hasNext()) {
                if (i > 0) {
                    // This means that the query came up with more than one result
                }
                Item item = iterator.next();
                item.asMap();
                i++;
            }
        }
        catch (Exception e) {

        }


        return null;
    }

    // TODO We usually don't want to send out a null variable without throwing an exception
    public <T extends DatabaseObject> List<T> getAll(String itemType) throws Exception {
        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#type", "item_type");

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":type", itemType);

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#type = :type").withNameMap(nameMap)
                .withValueMap(valueMap);

        ItemCollection<QueryOutcome> items = table.query(querySpec);
        Iterator<Item> iterator = items.iterator();
        List<T> allList = new ArrayList<>();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            allList.add((T)DatabaseObjectBuilder.build((Map<String, AttributeValue>)item));
        }

        return allList;
    }
}
