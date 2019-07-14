package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates an Event's description.
 */
public class EventUpdateDescription {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String description) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(Event.readEvent(eventID).owner) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update an event that you own!");
        }

        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateDescription(eventID, description));

        return databaseActions;
    }
}
