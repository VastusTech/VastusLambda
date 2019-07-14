package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Removes a tag from an Event.
 */
public class EventRemoveTag {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String tag) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Event event = Event.readEvent(eventID);

        if (fromID == null || (!fromID.equals(event.owner) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a gym you own!");
        }

        databaseActions.add(EventDatabaseActionBuilder.updateRemoveTag(eventID, tag));

        return databaseActions;
    }
}
