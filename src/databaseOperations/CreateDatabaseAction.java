package databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public class CreateDatabaseAction extends DatabaseAction {
    public CreateDatabaseAction(Map<String, AttributeValue> item) {
        action = DBAction.CREATE;
        this.item = item;

        // Unused variables
        this.updateAction = null;
        this.updateAttributeName = null;
        this.updateAttribute = null;
        this.checkHandler = null;
    }
}
