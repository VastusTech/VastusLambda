package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Sponsor;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateSponsorRequest;

import java.util.HashMap;
import java.util.Map;

public class SponsorDatabaseActionBuilder {
    final static private String itemType = "Sponsor";

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
        return new CreateDatabaseAction(item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
