package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

public class PostAddLike {
    public static List<DatabaseAction> getActions(String fromID, String postID, String like) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(like) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only like as yourself!");
        }

        // Add to the post
        databaseActions.add(PostDatabaseActionBuilder.updateAddLike(postID, like));

        // Also add to the user
        String likeItemType = ItemType.getItemType(like);
        databaseActions.add(UserDatabaseActionBuilder.updateAddLike(like, likeItemType, postID));

        return databaseActions;
    }
}
