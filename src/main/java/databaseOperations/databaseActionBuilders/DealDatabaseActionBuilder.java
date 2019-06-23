package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.databaseOperations.UpdateDatabaseAction;
import main.java.lambdaFunctionHandlers.requestObjects.CreateDealRequest;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

/**
 * The Database Action Builder for the {@link Deal} object, getting the {@link DatabaseAction} objects
 * that dictate the individual actions to do in the database for Groups.
 */
public class DealDatabaseActionBuilder {
    final static private String itemType = "Deal";

    /**
     * Gets the {@link PrimaryKey} object to identify the object in the database.
     *
     * @param id The ID of the object to reference.
     * @return The {@link PrimaryKey} object to identify the database item with.
     */
    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateDealRequest createDealRequest,
                                        Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Deal.getEmptyItem();
        item.put("sponsor", new AttributeValue(createDealRequest.sponsor));
        item.put("productName", new AttributeValue(createDealRequest.productName));
        item.put("productCreditPrice", new AttributeValue(createDealRequest.productCreditPrice));
        if (createDealRequest.quantity != null) { item.put("quantity",
                new AttributeValue(createDealRequest.quantity)); }
        if (createDealRequest.productImagePath != null) { item.put("productImagePath", new
                AttributeValue(createDealRequest.productImagePath)); }
        if (createDealRequest.productImagePaths != null) { item.put("productImagePaths",
                new AttributeValue(Arrays.asList(createDealRequest.productImagePaths))); }
        if (createDealRequest.validTime != null) { item.put("validTime", new AttributeValue(
                createDealRequest.validTime)); }
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
                (Map<String, AttributeValue> createdItem, String id) -> {
                    if (createDealRequest.productImagePath != null) {
                        createdItem.put("productImagePath", new AttributeValue(
                                id + "/" + createDealRequest.productImagePath
                        ));
                    }
                    if (createDealRequest.productImagePaths != null) {
                        List<String> productPaths = new ArrayList<>();
                        for (String productPath : createDealRequest.productImagePaths) {
                            productPaths.add(id + "/" + productPath);
                        }
                        createdItem.put("productImagePaths", new AttributeValue(productPaths));
                    }
                }
        );
    }

    public static DatabaseAction updateProductName(String id, String productName) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "productName", new AttributeValue(productName), false, PUT);
    }

    public static DatabaseAction updateProductImagePath(String id, String productImagePath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "productImagePath", new AttributeValue(productImagePath), false, PUT);
    }

    public static DatabaseAction updateAddProductImagePath(String id, String productImagePath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "productImagePaths", new AttributeValue(productImagePath), false, ADD);
    }

    public static DatabaseAction updateRemoveProductImagePath(String id, String productImagePath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "productImagePaths", new AttributeValue(productImagePath), false, DELETE);
    }

    public static DatabaseAction updateSetProductCreditPrice(String id, int productCreditPrice) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "productCreditPrice", new AttributeValue(Integer.toString(productCreditPrice)), false, PUT);
    }

    public static DatabaseAction updateValidTime(String id, String validTime) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "validTime", new AttributeValue(validTime), false, PUT);
    }

    public static DatabaseAction updateProductStoreLink(String id, String productStoreLink) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "productStoreLink", new AttributeValue(productStoreLink), false, PUT);
    }

    public static DatabaseAction updateSetQuantity(String id, int quantity) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "quantity", new AttributeValue().withN(Integer.toString(quantity)), false, PUT);
    }

    public static DatabaseAction updateAddQuantity(String id, int addQuantity) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "quantity", new AttributeValue().withN(Integer.toString(addQuantity)), false, ADD);
    }

    public static DatabaseAction updateRemoveQuantity(String id, int subtractQuantity) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "quantity", new AttributeValue().withN(Integer.toString(-subtractQuantity)), false, ADD, (newItem -> {
            if ((((Deal)newItem).quantity - subtractQuantity) < 0) {
                return "Not enough quantity in the Deal to perform the transaction!";
            }
            return null;
        }));
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
