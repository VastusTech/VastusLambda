package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * A DeleteDatabaseAction represents the action to delete an item in the database, conditionally or
 * otherwise. Conditionally means that if it passes the condition, then we delete the item.
 */
public class DeleteDatabaseAction extends DatabaseAction {
    public DeleteDatabaseAction(String id, String itemType, PrimaryKey primaryKey) {
        this.id = id;
        this.itemType = itemType;
        this.action = DBAction.DELETE;
        this.primaryKey = primaryKey;
        this.passoverIdentifiers = new HashMap<>();
    }

    public DeleteDatabaseAction(String id, String itemType, PrimaryKey primaryKey, CheckHandler
                                checkHandler) {
        this.id = id;
        this.itemType = itemType;
        this.action = DBAction.DELETECONDITIONAL;
        this.primaryKey = primaryKey;
        this.passoverIdentifiers = new HashMap<>();
        this.checkHandler = checkHandler;
    }
}
