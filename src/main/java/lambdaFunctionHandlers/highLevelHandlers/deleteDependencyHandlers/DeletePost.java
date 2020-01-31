package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes a Post from the database and any dependencies that may be tied to its Post ID.
 */
public class DeletePost {
    public static List<DatabaseAction> getActions(String fromID, String postID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Post post = Post.readPost(postID);

        if (fromID == null || (!fromID.equals(post.by) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only delete a post you authored!");
        }

        // Remove from posts by field
        String by = post.by;
        String byItemType = ItemType.getItemType(by);
        databaseActions.add(UserDatabaseActionBuilder.updateRemovePost(by, byItemType, postID));

        // Remove from liked
        for (String likedID : post.likes) {
            String likeItemType = ItemType.getItemType(likedID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveLike(likedID, likeItemType, postID));
        }

        // Remove from commented
        for (String commentID : post.comments) {
            databaseActions.addAll(DeleteComment.getActions(fromID, commentID));
        }

        // Remove from group
        if (post.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemovePost(post.group, postID));
        }

        // Delete the post
        databaseActions.add(PostDatabaseActionBuilder.delete(postID));

        return databaseActions;
    }
}
