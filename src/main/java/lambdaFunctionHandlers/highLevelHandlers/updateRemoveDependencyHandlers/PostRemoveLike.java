package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

/**
 * Removes a User from a Post's likes, and the Post from the User's liked.
 */
public class PostRemoveLike {
    public static List<DatabaseAction> getActions(String fromID, String postID, String like) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(like) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only remove like as yourself!");
        }

        // Remove from post
        databaseActions.add(PostDatabaseActionBuilder.updateRemoveLike(postID, like));

        // Also remove from the user
        String likeItemType = ItemType.getItemType(like);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveLike(like, likeItemType, postID));

        return databaseActions;
    }
}
