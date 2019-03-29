package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Comment;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.CommentDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class CommentUpdateComment {
    public static List<DatabaseAction> getActions(String fromID, String commentID, String commentString) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!(fromID.equals(Comment.readComment(commentID).by)) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a comment that you own!");
        }

        // Get all the actions for this process
        databaseActions.add(CommentDatabaseActionBuilder.updateComment(commentID, commentString));

        return databaseActions;
    }
}
