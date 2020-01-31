package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates an Event's restriction level, which forces User's to request joining the Event.
 */
public class EventUpdateRestriction {
    static public List<DatabaseAction> getActions(String fromID, String eventID, String restriction) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Event event = Event.readEvent(eventID);
        if (fromID == null || (!fromID.equals(event.owner) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update an challenge that you own!");
        }

        if (restriction != null && !restriction.equals("invite")) {
            throw new Exception("restriction value must be either nothing or \"invite\"");
        }

        databaseActions.add(EventDatabaseActionBuilder.updateRestriction(eventID, restriction));

        return databaseActions;
    }
}
