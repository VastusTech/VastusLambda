package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class PostRemovePicturePath {
    public static List<DatabaseAction> getActions(String fromID, String postID, String picturePath) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Post post = Post.readPost(postID);

        if (!fromID.equals(post.by) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a post you own!");
        }

        // Get all the actions for this process
        databaseActions.add(PostDatabaseActionBuilder.updateRemovePicturePath(postID, picturePath));

        return databaseActions;
    }
}
