package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class PostAddVideoPath {
    public static List<DatabaseAction> getActions(String fromID, String postID, String videoPath) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Post post = Post.readPost(postID);

        if (!fromID.equals(post.by) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a post you own!");
        }

        if (post.videoPaths != null && post.videoPaths.size() >= Constants.postVideoPathsLimit) {
            throw new Exception("This post is already filled with the max number of videos!");
        }

        // Get all the actions for this process
        databaseActions.add(PostDatabaseActionBuilder.updateAddVideoPath(postID, videoPath));

        return databaseActions;
    }
}