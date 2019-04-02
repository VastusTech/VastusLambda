package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Update a User's profile image path.
 */
public class UserUpdateProfileImagePath {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String
            profileImagePath) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a user you are!");
        }

        // Add the required actions
        databaseActions.add(UserDatabaseActionBuilder.updateProfileImagePath(userID, itemType, profileImagePath));

        return databaseActions;
    }
}
