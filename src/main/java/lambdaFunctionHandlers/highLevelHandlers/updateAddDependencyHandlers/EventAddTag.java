package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a tag to an Event, indicating what kind of Event it will be.
 */
public class EventAddTag {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String tag) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Event event = Event.readEvent(eventID);

        if (!fromID.equals(event.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }

        // TODO Check the tag name?

        databaseActions.add(EventDatabaseActionBuilder.updateAddTag(eventID, tag));

        return databaseActions;
    }
}
