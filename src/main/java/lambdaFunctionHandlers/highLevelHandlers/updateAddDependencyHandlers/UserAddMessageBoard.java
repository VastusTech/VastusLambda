package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

/**
 * Adds a message board to a user's message boards, showing up in their message board feed.
 */
public class UserAddMessageBoard {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String
              messageBoard) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(userID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a user you are!");
        }

        databaseActions.add(UserDatabaseActionBuilder.updateAddMessageBoard(userID, itemType, messageBoard));

        return databaseActions;
    }
}
