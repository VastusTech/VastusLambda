package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a profile image path to a User.
 */
public class UserAddProfileImagePath {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String
            profileImagePath) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(userID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a gym you own!");
        }

        databaseActions.add(UserDatabaseActionBuilder.updateAddProfileImagePath(userID, itemType, profileImagePath));

        return databaseActions;
    }
}
