package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes the Event from the database as well as remove any dependencies on its Event ID.
 */
public class DeleteEvent {
    public static List<DatabaseAction> getActions(String fromID, String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Event event = Event.readEvent(eventID);

        if (!fromID.equals(event.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete an event you own!");
        }

        // remove from owner's fields
        String ownerItemType = ItemType.getItemType(event.owner);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveOwnedEvent(event.owner, ownerItemType, eventID));

        // remove from each member's fields
        for (String member : event.members) {
            String memberItemType = ItemType.getItemType(member);
            User user = User.readUser(member, memberItemType);

            if (!event.ifCompleted) {
                // Remove the scheduled event if it's not completed
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledEvent(member, memberItemType, eventID));

                // Remove the scheduled time
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledTime(member, memberItemType, event.time
                        .toString()));
            }
            else {
                // Remove the completed event if it is completed
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveCompletedEvent(member, memberItemType, eventID));
            }

            // Also check their sentInvites and check to see if they sent any invites for this event
            for (String inviteID : user.sentInvites) {
                Invite invite = Invite.readInvite(inviteID);
                if (invite.about.equals(eventID)) {
                    databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
                }
            }
        }

        // Remove the receivedInvites
        for (String inviteID : event.receivedInvites) {
            databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
        }

        // Remove from the parent challenge
        if (event.challenge != null) {
            databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveEvent(event.challenge, eventID));
            databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveCompletedEvent(event.challenge, eventID));
        }

        // Remove from the parent group
        if (event.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveEvent(event.group, eventID));
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveCompletedEvent(event.group, eventID));
        }

        // Delete the challenge
        databaseActions.add(EventDatabaseActionBuilder.delete(eventID));

        return databaseActions;
    }
}
