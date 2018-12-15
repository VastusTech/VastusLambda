package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Invite;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
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
        Invite.InviteType inviteType = Invite.InviteType.valueOf(invite.inviteType);

        // TODO Check permissions
        switch (Invite.InviteType.valueOf(invite.inviteType)) {
            case friendRequest: {
                if (!fromID.equals(invite.to) && !fromID.equals(invite.from) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only delete a friend request you are a part of!");
                }
                break;
            }
            case eventInvite: {
                Event event = Event.readEvent(invite.about);
                if (!fromID.equals(invite.to) && !fromID.equals(invite.from) && !fromID.equals(event.owner) && !fromID.equals
                        (Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only delete an event invite you are a part of!");
                }
                break;
            }
            case challengeInvite: {
                Challenge challenge = Challenge.readChallenge(invite.about);
                if (!fromID.equals(invite.to) && !fromID.equals(invite.from) && !fromID.equals(challenge.owner) && !fromID
                        .equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only delete an challenge invite you are a part of!");
                }
                break;
            }
            case eventRequest: {
                Event event = Event.readEvent(invite.to);
                if (!fromID.equals(event.owner) && !fromID.equals(invite.from) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only delete an event request you are a part of!");
                }
                break;
            }
            case challengeRequest: {
                Challenge challenge = Challenge.readChallenge(invite.to);
                if (!fromID.equals(challenge.owner) && !fromID.equals(invite.from) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only delete an challenge request you are a part of!");
                }
                break;
            }
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        String fromItemType = ItemType.getItemType(invite.from);
        String toItemType = ItemType.getItemType(invite.to);

        switch (Invite.InviteType.valueOf(invite.inviteType)) {
            case friendRequest:
                // Remove from from's sentInvites field
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from to's receivedInvites field
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveReceivedInvite(invite.to, toItemType, inviteID));
                // Remove from friendRequests
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveFriendRequest(invite.to, toItemType, invite
                        .about));
                break;
            case eventInvite:
                // Remove from from's sentInvites field
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from to's receivedInvites field
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveReceivedInvite(invite.to, toItemType, inviteID));
                // Remove from invitedMembers and invitedEvents
                databaseActions.add(EventDatabaseActionBuilder.updateRemoveInvitedMember(invite.about, invite.to));
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveInvitedEvent(invite.to, toItemType, invite
                        .about));
                break;
            case challengeInvite:
                // Remove from from's sentInvites field
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from to's receivedInvites field
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveReceivedInvite(invite.to, toItemType, inviteID));
                // Remove from invitedMembers and invitedChallenges
                databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveInvitedMember(invite.about, invite.to));
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveInvitedChallenge(invite.to, toItemType, invite
                        .about));
                break;
            case eventRequest:
                // Remove from from's sentInvites field
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from the event's memberRequest
                databaseActions.add(EventDatabaseActionBuilder.updateRemoveMemberRequest(invite.to, invite.from));
                break;
            case challengeRequest:
                // Remove from from's sentInvites field
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from the challenge's memberRequest
                databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveMemberRequest(invite.to, invite.from));
                break;
        }

        // Delete the invite
        databaseActions.add(InviteDatabaseActionBuilder.delete(inviteID));

        return databaseActions.getDatabaseActions();
    }
}
