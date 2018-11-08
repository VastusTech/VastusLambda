package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientAddToEvent {
    public static List<DatabaseAction> getActions(String clientID, String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Event event = Event.readEvent(eventID);

        // Add to client's scheduled events
        databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledEvent(clientID,
                eventID, false));
        // Add to client's scheduled event times
        databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledTime(clientID, event.time.toString()));
        // Add to event's clients
        databaseActions.add(EventDatabaseActionBuilder.updateAddMember(eventID, clientID));

        return databaseActions;
    }
}
