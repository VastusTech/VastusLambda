package databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;

import java.util.Map;

public class UpdateDatabaseAction extends DatabaseAction {
    public UpdateDatabaseAction(Map<String, AttributeValue> key, Map<String, AttributeValueUpdate> updateItem) {
        this.action = DBAction.UPDATE;
        this.item = key;
        this.updateItem = updateItem;

        // Unused variables
        this.conditionalExpression = null;
    }

    public UpdateDatabaseAction(Map<String, AttributeValue> key, Map<String, AttributeValueUpdate> updateItem, String
            conditionalExpression) {
        this.action = DBAction.UPDATESAFE;
        this.item = key;
        this.updateItem = updateItem;
        this.conditionalExpression = conditionalExpression;
    }
}
