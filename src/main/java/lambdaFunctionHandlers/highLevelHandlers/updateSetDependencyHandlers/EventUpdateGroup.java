package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates an Event's associated Group.
 */
public class EventUpdateGroup {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String groupID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO Revisit
        Event event = Event.readEvent(eventID);
        Group group = null;
        if (groupID != null) {
            group = Group.readGroup(groupID);
        }

        if (!fromID.equals(event.owner) && (group == null || !group.owners.contains(fromID)) && !fromID.equals(Constants
                .adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a group that you own!");
        }

        if (event.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveEvent(event.group, eventID));
        }
        if (groupID != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateAddEvent(groupID, eventID, false));
        }

        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateGroup(eventID, groupID));

        return databaseActions;
    }
}
