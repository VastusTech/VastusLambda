package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public class DeleteDatabaseAction extends DatabaseAction {
    public DeleteDatabaseAction(Map<String, AttributeValue> key) {
        this.action = DBAction.DELETE;
        this.item = key;

        // Unused variables
        this.checkHandler = null;
        this.updateAction = null;
        this.updateAttribute = null;
        this.updateAttributeName = null;
    }

    public DeleteDatabaseAction(Map<String, AttributeValue> key, CheckHandler checkHandler) {
        this.action = DBAction.DELETECONDITIONAL;
        this.item = key;
        this.checkHandler = checkHandler;

        // Unused variables
        this.updateAction = null;
        this.updateAttribute = null;
        this.updateAttributeName = null;
    }
}
