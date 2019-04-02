package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a picture path onto an existing Post, making sure there aren't too many pictures in the post
 * already.
 */
public class PostAddPicturePath {
    public static List<DatabaseAction> getActions(String fromID, String postID, String picturePath) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Post post = Post.readPost(postID);

        if (!fromID.equals(post.by) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a post you own!");
        }

        if (post.picturePaths != null && post.picturePaths.size() >= Constants.postPicturePathsLimit) {
            throw new Exception("This post is already filled with the max number of pictures!");
        }

        // Get all the actions for this process
        databaseActions.add(PostDatabaseActionBuilder.updateAddPicturePath(postID, picturePath));

        return databaseActions;
    }
}
