package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateAddress {
    public static List<DatabaseAction> getActions(String eventID, String address) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO Check to see if the address is valid?

        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateAddress(eventID, address));

        return databaseActions;
    }
}
