package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserUpdateProfileImagePath {
    public static List<DatabaseAction> getActions(String id, String itemType, String profileImagePath) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO Is there a good way to check this?

        // Add the required actions
        databaseActions.add(UserDatabaseActionBuilder.updateProfileImagePath(id, itemType, profileImagePath));

        return databaseActions;
    }
}
