package databaseOperations;

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
import databaseObjects.DatabaseObject;
import databaseObjects.DatabaseObjectBuilder;

import java.util.*;

// TODO ALl of these classes should throw specific exceptions (Maybe that we define?)
public class DynamoDBHandler {
    String tableName;
    TransactionManager txManager;
    AmazonDynamoDB client;
    Table table;

    public DynamoDBHandler(String tableName) {
        this.tableName = tableName;
        // TODO AWS CREDENTIALS GOH HERES
        // AmazonDynamoDB client = new AmazonDynamoDBClient();
        // AmazonDynamoDBClient client2 = new AmazonDynamoDBAsyncClient();
        //AmazonDynamoDB client = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(new
                // AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1")).build());
        client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

        long waitTimeSeconds = 10 * 60;

        try {
            TransactionManager.verifyOrCreateTransactionTable(client, "Transactions", 10, 10, waitTimeSeconds);
            TransactionManager.verifyOrCreateTransactionImagesTable(client, "TransactionImages", 10, 10, waitTimeSeconds);
        }
        catch (InterruptedException ie) {

        }

        txManager = new TransactionManager(client, "Transactions", "TransactionImages");

        table = new Table(client, tableName);
    }

    // TODO Initialize txManager in here instead and make it static?
    // TODO The return value for this should be different
    public boolean attemptTransaction(List<DatabaseAction> databaseActions) {
        // TODO MARKER CHECK AND RETRY? Best bet seems like ConditionalCheckFailedException
        // We could just return it to the app so that it can choose to try again as it sees fit
        Transaction transaction = txManager.newTransaction();

        for (DatabaseAction databaseAction : databaseActions) {
            switch (databaseAction.action) {
                case CREATE:
                    try {
                        transaction.putItem(new PutItemRequest().withTableName(tableName).withItem(databaseAction.item));
                    }
                    catch (Exception e) {
                        System.out.println("Error creating item in the database.");
                        transaction.rollback();
                        return false;
                    }
                    break;
                case UPDATE:
                    try {
                        transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                (databaseAction.item).withAttributeUpdates(databaseAction.updateItem));

                        Map<String, AttributeValueUpdate> markerUpdate = new HashMap<>();
                        markerUpdate.put("marker", new AttributeValueUpdate(new AttributeValue().withN("1"), "ADD"));
                        transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                (databaseAction.item).withAttributeUpdates(markerUpdate));
                    }
                    catch (Exception e) {
                        System.out.println("Error updating the item in the database");
                        transaction.rollback();
                        return false;
                    }
                    break;
                case UPDATESAFE:
                    try {
                        transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                (databaseAction.item).withAttributeUpdates(databaseAction.updateItem)
                                .withConditionExpression(databaseAction.conditionalExpression));

                        Map<String, AttributeValueUpdate> markerUpdate = new HashMap<>();
                        markerUpdate.put("marker", new AttributeValueUpdate(new AttributeValue().withN("1"), "ADD"));
                        transaction.updateItem(new UpdateItemRequest().withTableName(tableName).withKey
                                (databaseAction.item).withAttributeUpdates(markerUpdate));
                    }
                    catch (ConditionalCheckFailedException ce) {
                        // TODO Implement this!
                    }
                    catch (Exception e) {
                        System.out.println("Error safely updating the item in the database");
                        transaction.rollback();
                        return false;
                    }
                    break;
                case DELETE:
                    try {
                        transaction.deleteItem(new DeleteItemRequest().withTableName(tableName).withKey
                                (databaseAction.item));
                    }
                    catch (Exception e) {
                        System.out.println("Error deleting the item in the database");
                        transaction.rollback();
                        return false;
                    }
                    break;
                case DELETESAFE:
                    try {
                        transaction.deleteItem(new DeleteItemRequest().withTableName(tableName).withKey
                                (databaseAction.item).withConditionExpression(databaseAction.conditionalExpression));
                    }
                    catch (ConditionalCheckFailedException ce) {
                        // TODO Implement this!
                        // This means that the app needs to try again
                    }
                    catch (Exception e) {
                        System.out.println("Error deleting the item in the database");
                        transaction.rollback();
                        return false;
                    }
                    break;
            }
        }

        // If it gets here, then the process completed safely.
        transaction.commit();
        transaction.delete();
        return true;
    }

//    public <T extends DatabaseObject> T readItem(Map<String, AttributeValue> key) {
//        Transaction transaction = txManager.newTransaction();
//        GetItemResult result;
//
//        try {
//            result = transaction.getItem(new GetItemRequest().withTableName(tableName).withKey(key));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        try {
//            return (T)DatabaseObjectBuilder.build(result.getItem());
//        }
//        catch (ClassCastException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public <T extends DatabaseObject> T readItem(Map<String, AttributeValue> key) {
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
    public <T extends DatabaseObject> T usernameQuery(String username, String itemType) {
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
    public <T extends DatabaseObject> List<T> getAll(String itemType) {
        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#type", "item_type");

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":type", itemType);

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#type = :type").withNameMap(nameMap)
                .withValueMap(valueMap);

        try {
            ItemCollection<QueryOutcome> items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            List<T> allList = new ArrayList<>();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                allList.add((T)DatabaseObjectBuilder.build((Map<String, AttributeValue>)item));
            }
            return allList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO This just shows you what to use for some things
//    public boolean doTransaction() {
//        Transaction transaction = txManager.newTransaction();
//
//        // TODO FOR A PUTITEM REQUEST, YOU NEED TO GIVE EVERY SINGLE ATTRIBUTE
//        Map<String, AttributeValue> item1 = new HashMap<>();
//        for (int i = 0; i < 10; i++) {
//            item1.put("ATTRIBUTENAME1", new AttributeValue("ATTRIBUTEVALUE"));
//            item1.put("ATTRIBUTENAME2", new AttributeValue(Arrays.asList("value1", "value2", "value3")));
//        }
//        // TODO FOR AN UPDATEITEM REQUEST,
//        Map<String, AttributeValueUpdate> item2Updates = new HashMap<>();
//        for (int i = 0; i < 10; i++) {
//            item2Updates.put("ATTRIBUTENAME1", new AttributeValueUpdate(new AttributeValue("ATTRIBUTEVALUE"), "SET"));
//            item2Updates.put("ATTRIBUTENAME2", new AttributeValueUpdate(new AttributeValue(Arrays.asList("value1", "value2", "value3")), "ADD"));
//            item2Updates.put("MARKER", new AttributeValueUpdate(new AttributeValue().withN("1"), "ADD"));
//        }
//        return false;
//    }

}
