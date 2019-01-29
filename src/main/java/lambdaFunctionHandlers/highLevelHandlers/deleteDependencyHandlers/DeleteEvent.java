package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.*;

import java.util.ArrayList;
import java.util.List;

public class DeleteEvent {
    public static List<DatabaseAction> getActions(String fromID, String eventID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Event event = Event.readEvent(eventID);

        if (!fromID.equals(event.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete an event you own!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

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

        // Remove from the challenge and from the group
        if (event.challenge != null) {
            databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveEvent(event.challenge, eventID));
            databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveCompletedEvent(event.challenge, eventID));
        }
        if (event.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveEvent(event.group, eventID));
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveCompletedEvent(event.group, eventID));
        }

        // Remove the receivedInvites
        for (String inviteID : event.receivedInvites) {
            databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
        }

        // Delete the challenge
        databaseActions.add(EventDatabaseActionBuilder.delete(eventID));

        return databaseActions;
    }
}
