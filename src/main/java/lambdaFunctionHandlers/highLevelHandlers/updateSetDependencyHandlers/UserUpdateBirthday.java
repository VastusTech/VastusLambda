package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class UserUpdateBirthday {
    public static List<DatabaseAction> getActions(String userID, String itemType, String birthday) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Check if it's a valid date
        new DateTime(birthday);
        // Get all the actions for this process
        databaseActions.add(UserDatabaseActionBuilder.updateBirthday(userID, itemType, birthday));

        return databaseActions;
    }
}
