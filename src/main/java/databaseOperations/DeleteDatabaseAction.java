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
    /**
     * Constructor for the delete database action.
     *
     * @param id The ID of the item to delete in the database.
     * @param itemType The type of the item to delete in the database.
     * @param primaryKey The {@link PrimaryKey} to reference the item with.
     */
    public DeleteDatabaseAction(String id, String itemType, PrimaryKey primaryKey) {
        this.id = id;
        this.itemType = itemType;
        this.action = DBAction.DELETE;
        this.primaryKey = primaryKey;
        this.passoverIdentifiers = new HashMap<>();
    }

    /**
     * Constructor for the delete database action for a delete conditional action, where an object
     * will get deleted only if the delete handler is satisfied.
     *
     * @param id The ID of the item to delete in the database.
     * @param itemType The type of the item to delete in the database.
     * @param primaryKey The {@link PrimaryKey} to reference the item with.
     * @param checkHandler The check handler to tell if the item should be deleted or not.
     */
    public DeleteDatabaseAction(String id, String itemType, PrimaryKey primaryKey, CheckHandler
                                checkHandler) {
        this.id = id;
        this.itemType = itemType;
        this.action = DBAction.DELETECONDITIONAL;
        this.primaryKey = primaryKey;
        this.passoverIdentifiers = new HashMap<>();
        this.checkHandler = checkHandler;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DeleteDatabaseAction) && obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
