package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserRemoveFriend {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String friendID)
            throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only unfriend people for yourself!");
        }

        // Get all the actions for this process
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveFriend(userID, itemType, friendID));

        // Mutual Friendship Removal
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveFriend(friendID, itemType, userID));

        return databaseActions;
    }
}
