package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

/**
 * Removes a User from a Submission's likes and the Submission from the User's liked.
 */
public class SubmissionRemoveLike {
    public static List<DatabaseAction> getActions(String fromID, String submissionID, String like) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(like) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only remove a like as yourself!");
        }

        // Remove from submission
        databaseActions.add(SubmissionDatabaseActionBuilder.updateRemoveLike(submissionID, like));

        // Also remove from the user
        String likeItemType = ItemType.getItemType(like);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveLike(like, likeItemType, submissionID));

        return databaseActions;
    }
}
