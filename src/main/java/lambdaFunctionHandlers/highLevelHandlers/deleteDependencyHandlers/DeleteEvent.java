package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteEvent {
    public static List<DatabaseAction> getActions(String fromID, String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Event event = Event.readEvent(eventID);

        if (!fromID.equals(event.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete an event you own!");
        }

        // TODO This is ripe for abuse...
        // TODO Do checking so that a cheeky guy can't just delete a challenge and make it so nobody wins?

        // remove from owner's fields
        String ownerItemType = ItemType.getItemType(event.owner);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveOwnedEvent(event.owner, ownerItemType, eventID));
        // remove from each member's fields
        for (String member : event.members) {
            String memberItemType = ItemType.getItemType(member);
            User user = User.readUser(member, memberItemType);
            // Remove the scheduled event
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledEvent(member, memberItemType, eventID));
            // Remove the scheduled time
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledTime(member, memberItemType, event.time
                    .toString()));
            // Remove the completed event as well to cover our bases
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveCompletedEvent(member, memberItemType, eventID));
            // Also check their sentInvites and check to see if they sent any invites for this event
            for (String inviteID : user.sentInvites) {
                Invite invite = Invite.readInvite(inviteID);
                if (invite.about.equals(eventID)) {
                    databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
                }
            }
        }

        // Delete the challenge
        databaseActions.add(EventDatabaseActionBuilder.delete(eventID));

        return databaseActions;
    }
}
