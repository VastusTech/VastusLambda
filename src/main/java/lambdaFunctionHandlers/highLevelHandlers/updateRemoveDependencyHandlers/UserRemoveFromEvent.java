package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.logic.Constants;
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
 * Removes a User from an Event and deletes any potential Invites the User sent for the Event.
 */
public class UserRemoveFromEvent {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String eventID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Event event = Event.readEvent(eventID);

        if (!fromID.equals(userID) && !fromID.equals(event.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only remove someone if you're the owner or that someone " +
                    "is you!");
        }

        if (!event.members.contains(userID)) {
            throw new Exception("You can't remove a user that isn't already in the event!");
        }

        if (event.owner.equals(userID)) {
            throw new Exception("You can't remove the owner from the event! Just delete the event in that case!");
        }

        // We delete the event from ourselves
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledEvent(userID, itemType, eventID));
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledTime(userID, itemType, event.time
                .toString()));

        // We also remove any invites that pertain to this event we may have sent
        User user = User.readUser(userID, itemType);
        for (String inviteID : user.sentInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(eventID)) {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }

        // And we delete ourselves from the challenge
        databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, userID));

        return databaseActions;
    }
}
