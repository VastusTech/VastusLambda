package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.ItemType;
import main.java.databaseObjects.Review;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ReviewDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteReview {
    public static List<DatabaseAction> getActions(String fromID, String reviewID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Review review = Review.readReview(reviewID);

        if (!fromID.equals(review.by) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a review you authored!");
        }

        // Remove from reviews about field
        String by = review.by;
        String byItemType = ItemType.getItemType(by);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveReviewBy(by, byItemType, reviewID));

        // Remove from reviews about field
        String about = review.about;
        String aboutItemType = ItemType.getItemType(about);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveReviewBy(about, aboutItemType, reviewID));

        // Delete the review
        databaseActions.add(ReviewDatabaseActionBuilder.delete(reviewID));

        return databaseActions;
    }
}
