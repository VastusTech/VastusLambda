package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Removes a video path from a Post.
 */
public class PostRemoveVideoPath {
    public static List<DatabaseAction> getActions(String fromID, String postID, String videoPath) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Post post = Post.readPost(postID);

        if (fromID == null || (!fromID.equals(post.by) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a post you own!");
        }

        // Get all the actions for this process
        databaseActions.add(PostDatabaseActionBuilder.updateRemoveVideoPath(postID, videoPath));

        return databaseActions;
    }
}
