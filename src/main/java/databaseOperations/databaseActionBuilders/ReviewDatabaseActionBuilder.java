package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Review;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateReviewRequest;

import java.util.HashMap;
import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class ReviewDatabaseActionBuilder {
    final static private String itemType = "Review";

    public static DatabaseAction create(CreateReviewRequest createReviewRequest) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Review.getEmptyItem();
        item.put("by", new AttributeValue(createReviewRequest.by));
        item.put("about", new AttributeValue(createReviewRequest.about));
        item.put("friendlinessRating", new AttributeValue(createReviewRequest.friendlinessRating));
        item.put("effectivenessRating", new AttributeValue(createReviewRequest.effectivenessRating));
        item.put("reliabilityRating", new AttributeValue(createReviewRequest.reliabilityRating));
        item.put("description", new AttributeValue(createReviewRequest.description));
        return new CreateDatabaseAction(item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction updateDescription(String id, String description) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "description", new AttributeValue(description), false, PUT);
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
