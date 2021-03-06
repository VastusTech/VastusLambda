package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
// import main.java.databaseObjects.Client;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteInvite;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a User to an Event, checking the validity, deleting any Invites associated with them, and
 * adds the Event to the User's schedule.
 */
public class UserAddToEvent {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String eventID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Event event = Event.readEvent(eventID);

        // Make sure they aren't already inside the event
        if (event.members.contains(userID)) {
            throw new Exception("That user is already in the event!");
        }

        if (event.ifCompleted) {
            throw new Exception("That event is already completed!");
        }

        boolean ifAcceptingRequest = false;

        if (event.memberRequests.contains(userID)) {
            if (fromID == null || (!fromID.equals(event.owner) && !Constants.isAdmin(fromID))) {
                throw new PermissionsException("Only the owner can accept your event member request!");
            }
            else {
                ifAcceptingRequest = true;
                databaseActions.add(EventDatabaseActionBuilder.updateRemoveMemberRequest(eventID, userID));
            }
        }
        else {
            if (fromID == null || (!fromID.equals(userID) && !Constants.isAdmin(fromID))) {
                throw new PermissionsException("Only you can add you to an event!");
            }
        }

        // Add to user's scheduled events
        databaseActions.add(UserDatabaseActionBuilder.updateAddScheduledEvent(userID, itemType,
                eventID, false));

        // Add to user's scheduled event times
        databaseActions.add(UserDatabaseActionBuilder.updateAddScheduledTime(userID, itemType, event.time.toString(),
         null));

        // Add to event's members
        databaseActions.add(EventDatabaseActionBuilder.updateAddMember(eventID, userID, ifAcceptingRequest));

        // Delete any potential invites associated with this
        User user = User.readUser(userID, itemType);
        for (String inviteID : user.receivedInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(eventID)) {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }
        for (String inviteID : event.receivedInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(userID)) {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }

        return databaseActions;
    }
}
