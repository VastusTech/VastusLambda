package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Comment;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.CommentDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the comment associated with a Comment.
 */
public class CommentUpdateComment {
    public static List<DatabaseAction> getActions(String fromID, String commentID, String commentString) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!(fromID.equals(Comment.readComment(commentID).by)) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a comment that you own!");
        }

        // Get all the actions for this process
        databaseActions.add(CommentDatabaseActionBuilder.updateComment(commentID, commentString));

        return databaseActions;
    }
}
