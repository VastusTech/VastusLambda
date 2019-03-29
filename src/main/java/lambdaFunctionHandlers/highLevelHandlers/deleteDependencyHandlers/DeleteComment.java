package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Comment;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.CommentDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class DeleteComment {
    public static List<DatabaseAction> getActions(String fromID, String commentID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Comment comment = Comment.readComment(commentID);

        if (!fromID.equals(comment.by) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a comment if you authored it!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        // Delete all comments underneath it, Delete it from the parent comment/post, and delete it from the user.
        for (String replyCommentID : comment.comments) {
            databaseActions.addAll(DeleteComment.getActions(fromID, replyCommentID));
        }
        String toItemType = ItemType.getItemType(comment.to);
        if (toItemType.equals("Post")) {
            databaseActions.add(PostDatabaseActionBuilder.updateRemoveComment(comment.to, commentID));
        }
        else if (toItemType.equals("Comment")) {
            databaseActions.add(CommentDatabaseActionBuilder.updateRemoveComment(comment.to, commentID));
        }
        String byItemType = ItemType.getItemType(comment.by);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveComment(comment.by, byItemType, commentID));

        // Delete the Client
        databaseActions.add(CommentDatabaseActionBuilder.delete(commentID));

        return databaseActions;
    }
}
