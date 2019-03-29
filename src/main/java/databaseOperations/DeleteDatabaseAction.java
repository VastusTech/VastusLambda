package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

/**
 * TODO
 */
public class DeleteDatabaseAction extends DatabaseAction {
    public DeleteDatabaseAction(String id, String itemType, PrimaryKey primaryKey) {
        this.id = id;
        this.itemType = itemType;
        this.action = DBAction.DELETE;
        this.primaryKey = primaryKey;
    }

    public DeleteDatabaseAction(String id, String itemType, PrimaryKey primaryKey, CheckHandler
                                checkHandler) {
        this.id = id;
        this.itemType = itemType;
        this.action = DBAction.DELETECONDITIONAL;
        this.primaryKey = primaryKey;
        this.checkHandler = checkHandler;
    }
}
