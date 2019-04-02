package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

/**
 * Updates whether an Event is completed or not in all the Event's dependencies.
 */
public class EventUpdateIfCompleted {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String ifCompleted) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Event event = Event.readEvent(eventID);

        if (!event.owner.equals(fromID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: In order to update an challenge, you need to be the owner!");
        }

        if (!ifCompleted.equals("true")) {
            if (ifCompleted.equals("false")) {
                throw new Exception("Cannot UN-complete an Event!");
            }
            else {
                throw new Exception("If Completed must be either \"true\" or \"false\"!");
            }
        }

        //  Finally remove the challenge from everyone's scheduled and into their completed
        for (String userID : event.members) {
            String userItemType = ItemType.getItemType(userID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledEvent(userID, userItemType, eventID));
            databaseActions.add(UserDatabaseActionBuilder.updateAddCompletedEvent(userID, userItemType, eventID));
        }

        // Updated Completed to Group as well
        if (event.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveEvent(event.group, eventID));
            databaseActions.add(GroupDatabaseActionBuilder.updateAddCompletedEvent(event.group, eventID));
        }

        // Set the event if completed to true
        databaseActions.add(EventDatabaseActionBuilder.updateIfCompleted(eventID, "true"));

        return databaseActions;
    }
}
