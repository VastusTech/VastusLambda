package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

public class PostRemoveLike {
    public static List<DatabaseAction> getActions(String fromID, String postID, String like) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(like) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only like as yourself!");
        }

        // Remove from post
        databaseActions.add(PostDatabaseActionBuilder.updateRemoveLike(postID, like));

        // Also remove from the user
        String likeItemType = ItemType.getItemType(like);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveLike(like, likeItemType, postID));

        return databaseActions;
    }
}
