package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Invite;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.InviteDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteInvite {
    public static List<DatabaseAction> getActions(String fromID, String inviteID) throws Exception {
        // List<DatabaseAction> databaseActions = new ArrayList<>();
        DatabaseActionCompiler databaseActions = new DatabaseActionCompiler();
        Invite invite = Invite.readInvite(inviteID);

        if (invite.inviteType.equals("friendRequest")) {
            if (!fromID.equals(invite.to) && !fromID.equals(invite.from) && !fromID.equals(Constants.adminKey)) {
                throw new Exception("PERMISSIONS ERROR: You can only delete a friend request you are a part of!");
            }
        }
        else if (invite.inviteType.equals("eventInvite")) {
            Event event = Event.readEvent(invite.about);
            if (!fromID.equals(invite.to) && !fromID.equals(invite.from) && !fromID.equals(event.owner) && !fromID.equals
                    (Constants.adminKey)) {
                throw new Exception("PERMISSIONS ERROR: You can only delete an event invite you are a part of!");
            }
        }

        // Remove from from's sentInvites field
        String fromItemType = ItemType.getItemType(invite.from);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));

        // Remove from to's receivedInvites field
        String toItemType = ItemType.getItemType(invite.to);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveReceivedInvite(invite.to, toItemType, inviteID));

        if (invite.inviteType.equals("friendRequest")) {
            // Remove from friendRequests
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveFriendRequest(invite.to, toItemType, invite
                    .about));
        }
        else if (invite.inviteType.equals("eventInvite")) {
            // Remove from invitedMembers and invitedEvents
            databaseActions.add(EventDatabaseActionBuilder.updateRemoveInvitedMember(invite.about, invite.to));
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveInvitedEvent(invite.to, toItemType, invite
                    .about));
        }

        // Delete the invite
        databaseActions.add(InviteDatabaseActionBuilder.delete(inviteID));

        return databaseActions.getDatabaseActions();
    }
}
