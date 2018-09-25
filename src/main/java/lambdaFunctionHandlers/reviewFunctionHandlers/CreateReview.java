package main.java.lambdaFunctionHandlers.reviewFunctionHandlers;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ReviewDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.WorkoutDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateReviewRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateReview {
    public static String handle(CreateReviewRequest createReviewRequest, String surveyWorkoutID) throws Exception {
        if (createReviewRequest != null) {
            if (createReviewRequest.byID != null && createReviewRequest.aboutID != null && createReviewRequest
                    .friendlinessRating != null && createReviewRequest.effectivenessRating != null &&
                    createReviewRequest.reliabilityRating != null && createReviewRequest.description != null) {
                List<DatabaseAction> databaseActions = new ArrayList<>();
                // Create Review
                databaseActions.add(ReviewDatabaseActionBuilder.create(createReviewRequest));
                // Add to by's reviews by
                String byID = createReviewRequest.byID;
                String byItemType = DatabaseObject.getItemType(byID);
                if (byItemType == null) {
                    throw new Exception("Review ByID is invalid!");
                }
                databaseActions.add(UserDatabaseActionBuilder.updateAddReviewBy(createReviewRequest.byID,
                        byItemType, null, true));
                // Add to about's reviews about
                String aboutID = createReviewRequest.aboutID;
                String aboutItemType = DatabaseObject.getItemType(aboutID);
                if (aboutItemType == null) {
                    throw new Exception("Review AboutID is invalid!");
                }
                databaseActions.add(UserDatabaseActionBuilder.updateAddReviewAbout(aboutID, aboutItemType,
                        null, true));
                // Calculate about's ratings
                Map<String, AttributeValue> aboutKey = new HashMap<>();
                aboutKey.put("item_type", new AttributeValue(aboutItemType));
                aboutKey.put("id", new AttributeValue(aboutID));
                User user = DynamoDBHandler.getInstance().readItem(aboutKey);
                float friendlinessRating = Float.parseFloat(createReviewRequest.friendlinessRating);
                float effectivenessRating = Float.parseFloat(createReviewRequest.effectivenessRating);
                float reliabilityRating = Float.parseFloat(createReviewRequest.reliabilityRating);
                int numReviews = user.reviewsAbout.size();

                // Basically it finds the "sum" of the ratings, using the current rating and the number of
                // reviews. Then, it adds our rating value to it, then divides it by numReviews + 1.
                float newFriendlinessRating = ((numReviews * user.friendlinessRating) +
                        friendlinessRating) / (numReviews + 1);
                float newEffectivenessRating = ((numReviews * user.effectivenessRating) +
                        effectivenessRating) / (numReviews + 1);
                float newReliabilityRating = ((numReviews * user.reliabilityRating) +
                        reliabilityRating) / (numReviews + 1);

                // Updates the about item
                databaseActions.add(UserDatabaseActionBuilder.updateFriendlinessRating(aboutID,
                        aboutItemType, Float.toString(newFriendlinessRating)));
                databaseActions.add(UserDatabaseActionBuilder.updateEffectivenessRating(aboutID,
                        aboutItemType, Float.toString(newEffectivenessRating)));
                databaseActions.add(UserDatabaseActionBuilder.updateReliabilityRating(aboutID,
                        aboutItemType, Float.toString(newReliabilityRating)));

                if (surveyWorkoutID != null) {
                    // TODO THIS SHOULD ALSO MOVE THE WORKOUT FROM SCHEDULED TO COMPLETED
                    databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveMissingReview(surveyWorkoutID, byID, true));
                }

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
            }
            else {
                throw new Exception("createReviewRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createReviewRequest not initialized for CREATE statement!");
        }
    }
}
