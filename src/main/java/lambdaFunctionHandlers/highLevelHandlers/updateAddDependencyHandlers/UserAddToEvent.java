package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Client;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteInvite;

import java.util.ArrayList;
import java.util.List;

public class UserAddToEvent {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String eventID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only add yourself to an event!");
        }

        // Get all the actions for this process
        Event event = Event.readEvent(eventID);

        // Make sure they aren't already inside the event
        if (event.members.contains(userID)) {
            throw new Exception("That user is already in the event!");
        }

        // Add to user's scheduled events
        databaseActions.add(UserDatabaseActionBuilder.updateAddScheduledEvent(userID, itemType,
                eventID, false));

        // Add to user's scheduled event times
        databaseActions.add(UserDatabaseActionBuilder.updateAddScheduledTime(userID, itemType, event.time.toString(),
         null));

        // Add to event's members
        databaseActions.add(EventDatabaseActionBuilder.updateAddMember(eventID, userID));

        // Delete any potential invites associated with this
        User user = User.readUser(userID, itemType);
        for (String inviteID : user.receivedInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(eventID)) {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }

        return databaseActions;
    }
}
