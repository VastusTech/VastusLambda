package databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import databaseObjects.Review;
import databaseOperations.CreateDatabaseAction;
import databaseOperations.DatabaseAction;
import databaseOperations.DeleteDatabaseAction;
import databaseOperations.UpdateDatabaseAction;
import lambdaFunctionHandlers.requestObjects.CreateReviewRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewDatabaseActionBuilder {
    final static private String itemType = "Review";

    public static DatabaseAction create(CreateReviewRequest createReviewRequest) {
        // TODO Make sure null values aren't problematic :p
        Map<String, AttributeValue> item = Review.getEmptyItem();
        item.put("byID", new AttributeValue(createReviewRequest.byID));
        item.put("aboutID", new AttributeValue(createReviewRequest.aboutID));
        item.put("friendliness_rating", new AttributeValue(createReviewRequest.friendlinessRating));
        item.put("effectiveness_rating", new AttributeValue(createReviewRequest.effectivenessRating));
        item.put("reliability_rating", new AttributeValue(createReviewRequest.reliabilityRating));
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
        key.put("item_type", new AttributeValue("Review"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}