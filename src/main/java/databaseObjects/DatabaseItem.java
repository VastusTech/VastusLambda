package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import main.java.databaseOperations.DynamoDBHandler;

/**
 * A DatabaseItem is the abstract item representing any item that exists within our database in any
 * table. Essentially is only given the ability to read and define the table name.
 */
abstract public class DatabaseItem {
    static public String tableName;

    static DatabaseItem read(String tableName, PrimaryKey primaryKey) throws Exception {
        return DynamoDBHandler.getInstance().readItem(tableName, primaryKey);
    }
}
