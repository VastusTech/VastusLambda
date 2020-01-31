package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Review;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateReviewRequest;

import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

/**
 * The Database Action Builder for the {@link Review} object, getting the {@link DatabaseAction} objects
 * that dictate the individual actions to do in the database for Reviews.
 */
public class ReviewDatabaseActionBuilder {
    final static private String itemType = "Review";

    /**
     * Gets the {@link PrimaryKey} object to identify the object in the database.
     *
     * @param id The ID of the object to reference.
     * @return The {@link PrimaryKey} object to identify the database item with.
     */
    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateReviewRequest createReviewRequest, Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Review.getEmptyItem();
        item.put("by", new AttributeValue(createReviewRequest.by));
        item.put("about", new AttributeValue(createReviewRequest.about));
        item.put("friendlinessRating", new AttributeValue(createReviewRequest.friendlinessRating));
        item.put("effectivenessRating", new AttributeValue(createReviewRequest.effectivenessRating));
        item.put("reliabilityRating", new AttributeValue(createReviewRequest.reliabilityRating));
        item.put("description", new AttributeValue(createReviewRequest.description));
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
            (Map<String, AttributeValue> createdItem, String id) -> {
                return;
            }
        );
    }

    public static DatabaseAction updateDescription(String id, String description) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "description", new AttributeValue(description), false, PUT);
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
