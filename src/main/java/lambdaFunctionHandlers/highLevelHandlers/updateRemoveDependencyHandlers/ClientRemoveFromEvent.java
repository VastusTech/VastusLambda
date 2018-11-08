package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientRemoveFromEvent {
    public static List<DatabaseAction> getActions(String clientID, String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Event event = Event.readEvent(eventID);

        // We delete the party from ourselves
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledEvent(clientID, eventID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(clientID, event.time
                .toString()));

        // And we delete ourselves from the challenge
        databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, clientID));

        return databaseActions;
    }
}
