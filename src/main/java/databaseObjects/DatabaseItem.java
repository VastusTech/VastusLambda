package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import main.java.databaseOperations.DynamoDBHandler;

/**
 * A DatabaseItem is the abstract item representing any item that exists within our database in any
 * table. Essentially is only given the ability to read and define the table name.
 */
abstract public class DatabaseItem {
    /**
     * Reads an item from any table in the database using a {@link PrimaryKey}.
     *
     * @param tableName The name of the table to get the item from.
     * @param primaryKey The {@link PrimaryKey} to identify the item with.
     * @return The DatabaseItem object received from the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    static DatabaseItem read(String tableName, PrimaryKey primaryKey) throws Exception {
        return DynamoDBHandler.getInstance().readItem(tableName, primaryKey);
    }
}
