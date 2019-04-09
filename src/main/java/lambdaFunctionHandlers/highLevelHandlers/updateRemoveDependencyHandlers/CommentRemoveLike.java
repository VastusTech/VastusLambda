package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.CommentDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.logic.Constants;
import main.java.logic.ItemType;

/**
 * Removes a User from the Comment's likes and a Comment from the User's liked.
 */
public class CommentRemoveLike {
    public static List<DatabaseAction> getActions(String fromID, String commentID, String like) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(like) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only remove a like as yourself!");
        }

        // Add to the post
        databaseActions.add(CommentDatabaseActionBuilder.updateRemoveLike(commentID, like));

        // Also add to the user
        String likeItemType = ItemType.getItemType(like);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveLike(like, likeItemType, commentID));

        return databaseActions;
    }
}
