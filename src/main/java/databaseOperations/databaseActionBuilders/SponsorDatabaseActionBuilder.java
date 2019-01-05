package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Sponsor;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateSponsorRequest;

import java.util.Map;

public class SponsorDatabaseActionBuilder {
    final static private String itemType = "Sponsor";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateSponsorRequest createSponsorRequest) {
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
        return new CreateDatabaseAction(itemType, item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(itemType, getPrimaryKey(id));
    }
}
