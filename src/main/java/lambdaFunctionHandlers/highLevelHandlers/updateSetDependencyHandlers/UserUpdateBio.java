package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class UserUpdateBio {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String bio) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a user you are!");
        }
        // Get all the actions for this process
        databaseActions.add(UserDatabaseActionBuilder.updateBio(userID, itemType, bio));

        return databaseActions;
    }
}
