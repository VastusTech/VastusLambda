package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Review;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.databaseOperations.UpdateDatabaseAction;
import main.java.lambdaFunctionHandlers.requestObjects.CreateReviewRequest;

import java.util.HashMap;
import java.util.Map;

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
        return new CreateDatabaseAction(item);
    }

//    public static DatabaseAction updateByID() {
//        return null;
//    }

//    public static DatabaseAction updateAboutID() {
//        return null;
//    }
//
//    public static DatabaseAction updateFriendlinessRating() {
//        return null;
//    }
//
//    public static DatabaseAction updateEffectivenessRating() {
//        return null;
//    }
//
//    public static DatabaseAction updateReliabilityRating() {
//        return null;
//    }

    public static DatabaseAction updateDescription(String id, String description) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "description", new AttributeValue(description), false, "SET");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
