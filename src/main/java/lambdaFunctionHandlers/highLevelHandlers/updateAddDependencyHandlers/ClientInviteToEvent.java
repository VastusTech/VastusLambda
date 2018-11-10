package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientInviteToEvent {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Event event = Event.readEvent(eventID);

        if (!event.members.contains(fromID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only invite someone to an event you're a part of!");
        }

        // Add to client's invited events
        databaseActions.add(ClientDatabaseActionBuilder.updateAddInvitedEvent(clientID, eventID));
        // Add to event's invited members
        databaseActions.add(EventDatabaseActionBuilder.updateAddInvitedMember(eventID, clientID));

        return databaseActions;
    }
}
