package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientRemoveFromEvent {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Event event = Event.readEvent(eventID);

        if (!fromID.equals(clientID) && !fromID.equals(event.owner) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only remove someone if you're the owner or that someone " +
                    "is you!");
        }

        // We delete the party from ourselves
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledEvent(clientID, eventID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(clientID, event.time
                .toString()));

        // And we delete ourselves from the challenge
        databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, clientID));

        return databaseActions;
    }
}
