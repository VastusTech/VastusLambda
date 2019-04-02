package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

/**
 * Remove a message board from a User's boards, so it won't show up in their feed.
 */
public class UserRemoveMessageBoard {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String
              messageBoard) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a user you are!");
        }

        databaseActions.add(UserDatabaseActionBuilder.updateRemoveMessageBoard(userID, itemType, messageBoard));

        return databaseActions;
    }
}
