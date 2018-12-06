package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserAddProfileImagePath {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String
            profileImagePath) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }

        databaseActions.add(UserDatabaseActionBuilder.updateAddProfileImagePath(userID, itemType, profileImagePath));

        return databaseActions;
    }
}
