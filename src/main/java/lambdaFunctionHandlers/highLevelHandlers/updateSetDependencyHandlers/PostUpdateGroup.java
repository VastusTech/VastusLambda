package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class PostUpdateGroup {
    public static List<DatabaseAction> getActions(String fromID, String postID, String groupID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Post post = Post.readPost(postID);
        Group group = null;
        if (groupID != null) {
            group = Group.readGroup(groupID);
        }

        if (!fromID.equals(post.by) && (group == null || !group.owners.contains(fromID)) && !fromID.equals(Constants
                .adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a post that you own!");
        }

        if (post.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemovePost(post.group, postID));
        }
        if (groupID != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateAddPost(groupID, postID, false));
        }

        // Get all the actions for this process
        databaseActions.add(PostDatabaseActionBuilder.updateGroup(postID, groupID));

        return databaseActions;
    }
}
