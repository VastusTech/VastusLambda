package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

import main.java.databaseObjects.Admin;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.lambdaFunctionHandlers.requestObjects.CreateAdminRequest;

public class AdminDatabaseActionBuilder {
    final static private String itemType = "Admin";

    /**
     * Gets the {@link PrimaryKey} object to identify the object in the database.
     *
     * @param id The ID of the object to reference.
     * @return The {@link PrimaryKey} object to identify the database item with.
     */
    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateAdminRequest createAdminRequest, Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Admin.getEmptyItem();
        item.put("name", new AttributeValue(createAdminRequest.name));
        item.put("email", new AttributeValue(createAdminRequest.email));
        item.put("username", new AttributeValue(createAdminRequest.username));
        if (createAdminRequest.birthday != null) { item.put("birthday", new AttributeValue(createAdminRequest
                .birthday)); }
        if (createAdminRequest.bio != null) { item.put("bio", new AttributeValue(createAdminRequest.bio)); }
        if (createAdminRequest.stripeID != null) { item.put("stripeID", new AttributeValue(createAdminRequest
                .stripeID)); }
        if (createAdminRequest.federatedID != null) { item.put("federatedID", new AttributeValue(createAdminRequest
                .federatedID)); }
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
                (Map<String, AttributeValue> createdItem, String id) -> {
                    return;
                }
        );
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
