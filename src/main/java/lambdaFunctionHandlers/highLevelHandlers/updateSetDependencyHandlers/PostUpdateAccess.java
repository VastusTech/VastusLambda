package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Update the access associated with a Post.
 */
public class PostUpdateAccess {
    public static List<DatabaseAction> getActions(String fromID, String postID, String access) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Post post = Post.readPost(postID);

        if (!fromID.equals(post.by) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a post you own!");
        }

        // Check to see if the access is valid
        if (!access.equals("private") && !access.equals("public")) {
            throw new Exception("Challenge access must be \"public\" or \"private\"!");
        }

        if (access.equals("public") && ItemType.getItemType(Post.readPost(postID).by).equals("Client")) {
            throw new Exception("A Client cannot own a public Post!");
        }

        // Get all the actions for this process
        databaseActions.add(PostDatabaseActionBuilder.updateAccess(postID, access));

        return databaseActions;
    }
}
