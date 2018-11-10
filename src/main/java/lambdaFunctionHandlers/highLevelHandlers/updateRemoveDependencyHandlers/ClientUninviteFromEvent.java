package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseObjects.Client;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientUninviteFromEvent {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(Event.readEvent(eventID).owner) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only uninvite members from a event that you own!");
        }

        // Remove from our own attributes
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveInvitedEvent(clientID, eventID));
        // Remove from the event's attributes
        databaseActions.add(EventDatabaseActionBuilder.updateRemoveInvitedMember(eventID, clientID));

        return databaseActions;
    }
}
