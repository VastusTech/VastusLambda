package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public class DeleteDatabaseAction extends DatabaseAction {
    public DeleteDatabaseAction(Map<String, AttributeValue> key) {
        this.action = DBAction.DELETE;
        this.item = key;
    }

    public DeleteDatabaseAction(Map<String, AttributeValue> key, CheckHandler checkHandler) {
        this.action = DBAction.DELETECONDITIONAL;
        this.item = key;
        this.checkHandler = checkHandler;
    }
}
