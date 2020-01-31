package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

/**
 * Adds a User to a Submission's likes and a Submission to a User's likes.
 */
public class SubmissionAddLike {
    public static List<DatabaseAction> getActions(String fromID, String submissionID, String like) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(like) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only like as yourself!");
        }

        // Add to the post
        databaseActions.add(SubmissionDatabaseActionBuilder.updateAddLike(submissionID, like));

        // Also add to the user
        String likeItemType = ItemType.getItemType(like);
        databaseActions.add(UserDatabaseActionBuilder.updateAddLike(like, likeItemType, submissionID));

        return databaseActions;
    }
}
