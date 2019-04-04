package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Invite;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.InviteDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.List;

/**
 * Deletes an Invite from the database and removes all the dependencies on its Invite ID.
 */
public class DeleteInvite {
    public static List<DatabaseAction> getActions(String fromID, String inviteID) throws Exception {
        DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();
        Invite invite = Invite.readInvite(inviteID);
        Invite.InviteType inviteType = Invite.InviteType.valueOf(invite.inviteType);

//        public String from;
//        public String to;
//        public String inviteType;
//        public String about;

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

        String fromItemType = ItemType.getItemType(invite.from);
        String toItemType = ItemType.getItemType(invite.to);

        switch (Invite.InviteType.valueOf(invite.inviteType)) {
            case friendRequest:
                // Remove from from's sentInvites field
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from to's receivedInvites field
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveReceivedInvite(invite.to, toItemType, inviteID));
                // Remove from friendRequests
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveFriendRequest(invite.to, toItemType, invite
                        .about));
                break;
            case eventInvite:
                // Remove from from's sentInvites field
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from to's receivedInvites field
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveReceivedInvite(invite.to, toItemType, inviteID));
                // Remove from invitedMembers and invitedEvents
                databaseActionCompiler.add(EventDatabaseActionBuilder.updateRemoveInvitedMember(invite.about, invite.to));
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveInvitedEvent(invite.to, toItemType, invite
                        .about));
                break;
            case challengeInvite:
                // Remove from from's sentInvites field
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from to's receivedInvites field
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveReceivedInvite(invite.to, toItemType, inviteID));
                // Remove from invitedMembers and invitedChallenges
                databaseActionCompiler.add(ChallengeDatabaseActionBuilder.updateRemoveInvitedMember(invite.about, invite.to));
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveInvitedChallenge(invite.to, toItemType, invite
                        .about));
                break;
            case eventRequest:
                // Remove from from's sentInvites field
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from the event's memberRequest
                databaseActionCompiler.add(EventDatabaseActionBuilder.updateRemoveMemberRequest(invite.to, invite.from));
                break;
            case challengeRequest:
                // Remove from from's sentInvites field
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateRemoveSentInvite(invite.from, fromItemType, inviteID));
                // Remove from the challenge's memberRequest
                databaseActionCompiler.add(ChallengeDatabaseActionBuilder.updateRemoveMemberRequest(invite.to, invite.from));
                break;
        }

        // Send an Ably message!
//        JsonObjectBuilder payload = Json.createObjectBuilder()
//                .add("id", invite.id);
//
//        if (toItemType.equals("Client") || toItemType.equals("Trainer") || toItemType.equals("Gym")) {
//            // Send to the person
//            databaseActionCompiler.addMessage(invite.to + "-Notifications", "DeleteInvite", payload);
//        } else if (toItemType.equals("Event")) {
//            // Send to the owner
//            databaseActionCompiler.addMessage(Event.readEvent(invite.to).owner + "-Notifications", "DeleteInvite", payload);;
//        } else if (toItemType.equals("Challenge")) {
//            // Send to the owner
//            databaseActionCompiler.addMessage(Challenge.readChallenge(invite.to).owner + "-Notifications", "DeleteInvite", payload);;
//        } else if (toItemType.equals("Group")) {
//            // Send to the owners
//            for (String owner : Group.readGroup(invite.to).owners) {
//                databaseActionCompiler.addMessage(owner + "-Notifications", "DeleteInvite", payload);
//            }
//        }
//        else {
//            Constants.debugLog("SENDING MESSAGE NOT HANDLED FOR INVITE TO THAT ITEM TYPE = " + toItemType);
//        }

        // Delete the invite
        databaseActionCompiler.add(InviteDatabaseActionBuilder.delete(inviteID));

        return databaseActionCompiler.getDatabaseActions();
    }
}
