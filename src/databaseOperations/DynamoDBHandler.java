package databaseOperations;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.TransactionManager;

import java.util.*;

public class DynamoDBHandler {
    String tableName;
    TransactionManager txManager;

    public DynamoDBHandler(String tableName) {
        this.tableName = tableName;
        // TODO AWS CREDENTIALS GOH HERES
        // AmazonDynamoDB client = new AmazonDynamoDBClient();
        // AmazonDynamoDBClient client2 = new AmazonDynamoDBAsyncClient();
        //AmazonDynamoDB client = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(new
                // AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1")).build());
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();

        long waitTimeSeconds = 10 * 60;

        try {
            TransactionManager.verifyOrCreateTransactionTable(client, "Transactions", 10, 10, waitTimeSeconds);
            TransactionManager.verifyOrCreateTransactionImagesTable(client, "TransactionImages", 10, 10, waitTimeSeconds);
        }
        catch (InterruptedException ie) {

        }

        txManager = new TransactionManager(client, "Transactions", "TransactionImages");
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
