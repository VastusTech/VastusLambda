package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserUpdateEmail {
    public static List<DatabaseAction> getActions(String userID, String itemType, String email) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        databaseActions.add(UserDatabaseActionBuilder.updateEmail(userID, itemType, email));

        return databaseActions;
    }
}
