package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import main.java.databaseOperations.DynamoDBHandler;

abstract public class DatabaseItem {
    static public String tableName;

    static DatabaseItem read(String tableName, PrimaryKey primaryKey) throws Exception {
        return DynamoDBHandler.getInstance().readItem(tableName, primaryKey);
    }
}
