package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteEvent {
    public static List<DatabaseAction> getActions(String fromID, String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Event event = Event.readEvent(eventID);

        if (!fromID.equals(event.owner) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only delete an event you own!");
        }

        // TODO This is ripe for abuse...
        // TODO Do checking so that a cheeky guy can't just delete a challenge and make it so nobody wins?

        // remove from owner's fields
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveOwnedEvent(event.owner, eventID));
        // remove from each member's fields
        for (String member : event.members) {
            // Remove the scheduled event
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledEvent(member, eventID));
            // Remove the scheduled time
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(member, event.time.toString()));
        }
        // remove from each invited member's fields
        for (String invitedMember : event.invitedMembers) {
            // Remove the invited event
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveInvitedEvent(invitedMember, eventID));
        }

        // Delete the challenge
        databaseActions.add(EventDatabaseActionBuilder.delete(eventID));

        return databaseActions;
    }
}
