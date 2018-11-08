package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteEvent {
    public static List<DatabaseAction> getActions(String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Event event = Event.readEvent(eventID);

        //Constants.debugLog("Getting delete challenge actions");

        // TODO This is ripe for abuse...
        // TODO Do checking so that a cheeky guy can't just delete a challenge and make it so nobody wins?

        // remove from owner's fields
        //Constants.debugLog("Removing from owner's owned events field");
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveOwnedEvent(event.owner, eventID));
        // remove from each member's fields
        //Constants.debugLog("Removing from all the members scheduled challenges: members size = " + challenge.members.size());
        for (String member : event.members) {
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledEvent(member, eventID));
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(member, event.time.toString()));
        }

        // Delete the challenge
        databaseActions.add(EventDatabaseActionBuilder.delete(eventID));

        return databaseActions;
    }
}
