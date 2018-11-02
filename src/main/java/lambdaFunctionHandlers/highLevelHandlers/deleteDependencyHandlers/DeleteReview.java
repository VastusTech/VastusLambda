package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

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
        String by = review.by;
        String byItemType = DatabaseObject.getItemType(by);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveReviewBy(by, byItemType, reviewID));
        // Remove from reviews about field
        String about = review.about;
        String aboutItemType = DatabaseObject.getItemType(about);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveReviewBy(about, aboutItemType, reviewID));

        // Delete the review
        databaseActions.add(ReviewDatabaseActionBuilder.delete(reviewID));

        return databaseActions;
    }
}
