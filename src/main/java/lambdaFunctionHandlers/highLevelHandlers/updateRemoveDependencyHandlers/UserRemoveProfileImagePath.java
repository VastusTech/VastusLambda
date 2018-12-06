package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserRemoveProfileImagePath {
    public static List<DatabaseAction> getActions(String fromID, String userID, String profileImagePath) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }

        String userItemType = ItemType.getItemType(userID);

        databaseActions.add(UserDatabaseActionBuilder.updateRemoveProfileImagePath(userID, userItemType, profileImagePath));

        return databaseActions;
    }
}
