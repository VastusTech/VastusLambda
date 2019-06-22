package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Sponsor;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateSponsorRequest;

import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.ADD;
import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.DELETE;

/**
 * The Database Action Builder for the {@link Sponsor} object, getting the {@link DatabaseAction} objects
 * that dictate the individual actions to do in the database for Sponsors.
 */
public class SponsorDatabaseActionBuilder {
    final static private String itemType = "Sponsor";

    /**
     * Gets the {@link PrimaryKey} object to identify the object in the database.
     *
     * @param id The ID of the object to reference.
     * @return The {@link PrimaryKey} object to identify the database item with.
     */
    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateSponsorRequest createSponsorRequest, Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Sponsor.getEmptyItem();
        item.put("name", new AttributeValue(createSponsorRequest.name));
        item.put("email", new AttributeValue(createSponsorRequest.email));
        item.put("username", new AttributeValue(createSponsorRequest.username));
        if (createSponsorRequest.birthday != null) { item.put("birthday", new AttributeValue(createSponsorRequest
                .birthday)); }
        if (createSponsorRequest.bio != null) { item.put("bio", new AttributeValue(createSponsorRequest.bio)); }
        if (createSponsorRequest.stripeID != null) { item.put("stripeID", new AttributeValue(createSponsorRequest
                .stripeID)); }
        if (createSponsorRequest.federatedID != null) { item.put("federatedID", new AttributeValue(createSponsorRequest
                .federatedID)); }
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
            (Map<String, AttributeValue> createdItem, String id) -> {
                return;
            }
        );
    }

    public static DatabaseAction updateAddDeal(String id, String deal, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "deals", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "deals", new AttributeValue(deal), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveDeal(String id, String deal) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "deals", new AttributeValue(deal), false, DELETE);
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
