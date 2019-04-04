package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;
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
 * Deletes a Comment from the database as well as any dependencies on its Comment ID.
 */
public class DeleteComment {
    public static List<DatabaseAction> getActions(String fromID, String commentID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Comment comment = Comment.readComment(commentID);

        if (!fromID.equals(comment.by) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a comment if you authored it!");
        }

        // Remove from the by's comments
        String byItemType = ItemType.getItemType(comment.by);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveComment(comment.by, byItemType, commentID));

        // Remove from the to's comments
        String toItemType = ItemType.getItemType(comment.to);
        if (toItemType.equals("Post")) {
            databaseActions.add(PostDatabaseActionBuilder.updateRemoveComment(comment.to, commentID));
        }
        else if (toItemType.equals("Comment")) {
            databaseActions.add(CommentDatabaseActionBuilder.updateRemoveComment(comment.to, commentID));
        }
        else if (toItemType.equals("Submission")) {
            databaseActions.add(SubmissionDatabaseActionBuilder.updateRemoveComment(comment.to, commentID));
        }
        else {
            throw new Exception("Could not use to ID of item type = " + toItemType);
        }

        // Delete all comments underneath it, Delete it from the parent comment/post, and delete it from the user.
        for (String replyCommentID : comment.comments) {
            databaseActions.addAll(DeleteComment.getActions(fromID, replyCommentID));
        }

        // Delete all the likes
        for (String likeID : comment.likes) {
            String likeItemType = ItemType.getItemType(likeID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveLike(likeID, likeItemType, commentID));
        }

        // Delete the Client
        databaseActions.add(CommentDatabaseActionBuilder.delete(commentID));

        return databaseActions;
    }
}
