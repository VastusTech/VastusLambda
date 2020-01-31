package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Arrays;
import java.util.Map;

import main.java.databaseObjects.Product;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.databaseOperations.UpdateDatabaseAction;
import main.java.lambdaFunctionHandlers.requestObjects.CreateProductRequest;

/**
 * The Database Action Builder for the {@link Product} object, getting the {@link DatabaseAction} objects
 * that dictate the individual actions to do in the database for Groups.
 */
public class ProductDatabaseActionBuilder {
    final static private String itemType = "Product";

    /**
     * Gets the {@link PrimaryKey} object to identify the object in the database.
     *
     * @param id The ID of the object to reference.
     * @return The {@link PrimaryKey} object to identify the database item with.
     */
    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateProductRequest createProductRequest,
                                        Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Product.getEmptyItem();
        item.put("owner", new AttributeValue(createProductRequest.owner));
        item.put("deal", new AttributeValue(createProductRequest.deal));
        if (createProductRequest.expirationDate != null) { item.put("expirationDate",
                new AttributeValue(createProductRequest.expirationDate)); }
        if (createProductRequest.codes != null) { item.put("codes",
                new AttributeValue(Arrays.asList(createProductRequest.codes))); }
        if (createProductRequest.links != null) { item.put("links",
                new AttributeValue(Arrays.asList(createProductRequest.links))); }
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
                (Map<String, AttributeValue> createdItem, String id) -> {}
        );
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
