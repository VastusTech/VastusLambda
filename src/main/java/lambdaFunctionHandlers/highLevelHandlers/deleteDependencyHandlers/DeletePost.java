package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeletePost {
    public static List<DatabaseAction> getActions(String fromID, String postID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Post post = Post.readPost(postID);

        if (!fromID.equals(post.by) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a post you authored!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        // Remove from posts by field
        String by = post.by;
        String byItemType = ItemType.getItemType(by);
        databaseActions.add(UserDatabaseActionBuilder.updateRemovePost(by, byItemType, postID));

        // Check if we are also removing this post from the challenge submissions
        if (post.postType != null && post.postType.equals("submission")) {
            databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveSubmission(post.about, post.id));
        }

        // Remove from group
        if (post.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemovePost(post.group, postID));
        }

        for (String commentID : post.comments) {
            databaseActions.addAll(DeleteComment.getActions(fromID, commentID));
        }

        // Delete the post
        databaseActions.add(PostDatabaseActionBuilder.delete(postID));

        return databaseActions;
    }
}
