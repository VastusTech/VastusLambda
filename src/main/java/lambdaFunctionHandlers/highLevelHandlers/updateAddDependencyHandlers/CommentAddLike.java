package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.CommentDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.logic.ItemType;

/**
 * Adds a User to a Comment's liked and a Comment to a User's liked.
 */
public class CommentAddLike {
    public static List<DatabaseAction> getActions(String fromID, String commentID, String like) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(like) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only like as yourself!");
        }

        // Add to the post
        databaseActions.add(CommentDatabaseActionBuilder.updateAddLike(commentID, like));

        // Also add to the user
        String likeItemType = ItemType.getItemType(like);
        databaseActions.add(UserDatabaseActionBuilder.updateAddLike(like, likeItemType, commentID));

        return databaseActions;
    }
}
