package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

import main.java.databaseObjects.Enterprise;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.lambdaFunctionHandlers.requestObjects.CreateEnterpriseRequest;

public class EnterpriseDatabaseActionBuilder {
    final static private String itemType = "Enterprise";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateEnterpriseRequest createEnterpriseRequest, boolean ifWithCreate) {
        Map<String, AttributeValue> item = Enterprise.getEmptyItem();
        // TODO Update with Enterprise creation process!
        return new CreateDatabaseAction(itemType, item, ifWithCreate,
            (Map<String, AttributeValue> createdItem, String id) -> {
                return;
            }
        );
    }

    // TODO Update with updating methods

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
