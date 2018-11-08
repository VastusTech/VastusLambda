package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateIfChallenge {
    public static List<DatabaseAction> getActions(String eventID, String ifChallenge) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Check to see if it's a properly formed boolean
        Boolean.parseBoolean(ifChallenge);

        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateIfChallenge(eventID, ifChallenge));

        return databaseActions;
    }
}
