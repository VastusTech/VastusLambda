package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Review;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ReviewDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class DeleteReview {
    public static List<DatabaseAction> getActions(String fromID, String reviewID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Review review = Review.readReview(reviewID);

        if (fromID == null || (!fromID.equals(review.by) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only delete a review you authored!");
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
