package databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public class DeleteDatabaseAction extends DatabaseAction {
    public DeleteDatabaseAction(Map<String, AttributeValue> key) {
        this.action = DBAction.DELETE;
        this.item = key;

        // Unused variables
        this.updateItem = null;
        this.conditionalExpression = null;
    }

    public DeleteDatabaseAction(Map<String, AttributeValue> key, String conditionalExpression) {
        this.action = DBAction.DELETESAFE;
        this.item = key;
        this.conditionalExpression = conditionalExpression;

        // Unused variables
        this.updateItem = null;
    }
}
