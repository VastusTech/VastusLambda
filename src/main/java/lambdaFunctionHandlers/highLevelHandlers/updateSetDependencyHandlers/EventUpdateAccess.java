package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateAccess {
    public static List<DatabaseAction> getActions(String eventID, String access) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!access.equals("private") && !access.equals("public")) {
            throw new Exception("Challenge access must be \"public\" or \"private\"!");
        }

        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateAccess(eventID, access));

        return databaseActions;
    }
}
