package main.java.lambdaFunctionHandlers.reviewFunctionHandlers;

import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.Review;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ReviewDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteReview {
    public static List<DatabaseAction> getActions(String reviewID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Review review = Review.readReview(reviewID);
        // Remove from reviews about field
        String byID = review.byID;
        String byItemType = DatabaseObject.getItemType(byID);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveReviewBy(byID, byItemType, reviewID));
        // Remove from reviews about field
        String aboutID = review.aboutID;
        String aboutItemType = DatabaseObject.getItemType(aboutID);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveReviewBy(aboutID, aboutItemType, reviewID));

        // Delete the review
        databaseActions.add(ReviewDatabaseActionBuilder.delete(reviewID));

        return getActions(reviewID);
    }
}
