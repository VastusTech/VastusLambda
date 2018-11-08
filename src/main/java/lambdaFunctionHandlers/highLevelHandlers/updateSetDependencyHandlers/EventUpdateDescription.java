package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateDescription {
    public static List<DatabaseAction> getActions(String eventID, String description) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateDescription(eventID, description));

        return databaseActions;
    }
}
