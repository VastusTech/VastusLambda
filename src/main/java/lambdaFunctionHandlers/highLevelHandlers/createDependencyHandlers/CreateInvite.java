package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.Invite;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateInviteRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates an Invite in the database, checks the inputs, and adds it to the sender's sent
 * invitations, the receiver's received invitations, and sends a notification to the receiver.
 */
public class CreateInvite {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateInviteRequest createInviteRequest, int depth) throws Exception {
        if (createInviteRequest != null) {
            // Create client
            if (createInviteRequest.from != null && createInviteRequest.to != null && createInviteRequest
                    .inviteType != null && createInviteRequest.about != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (fromID == null || (!(fromID.equals(createInviteRequest.from)) && !Constants.isAdmin(fromID))) {
                    throw new PermissionsException("You can only send invites you have created!");
                }

                Invite.InviteType inviteType;
                try {
                    inviteType = Invite.InviteType.valueOf(createInviteRequest.inviteType);
                }
                catch (IllegalArgumentException e) {
                    throw new Exception("Could not understand inviteType: " + createInviteRequest.inviteType);
                }

                // Check to see if the request features are well formed (i.e not invalid date or time)

                if (createInviteRequest.to.equals(createInviteRequest.from)) {
                    throw new Exception("Can't send an invite to yourself!");
                }

                // Add the create statement
                // TODO If we ever try to create Invites automatically, figure out which
                // TODO attributes need which passover Identifiers.
                databaseActionCompiler.add(InviteDatabaseActionBuilder.create(createInviteRequest, null));

                // Then we add it to the object's stuff
                // Add to the from's sentInvites
                String fromItemType = ItemType.getItemType(createInviteRequest.from);
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddSentInvite(createInviteRequest.from,
                        fromItemType, null, true));

                // Get to itemType
                String toItemType = ItemType.getItemType(createInviteRequest.to);

                switch (inviteType) {
                    case friendRequest:
                        if (!(fromID.equals(createInviteRequest.about)) && !fromID.equals(Constants.adminKey)) {
                            throw new Exception("PERMISSIONS ERROR: You can only send friend requests for yourself!");
                        }
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddFriendRequest(createInviteRequest
                                .to, toItemType, createInviteRequest.about));

                        // Add to the to's receivedInvites
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddReceivedInvite(createInviteRequest.to,
                                toItemType, null, true));
                        break;
                    case eventInvite:
                        Event event = Event.readEvent(createInviteRequest.about);
                        if (!event.members.contains(fromID) && !fromID.equals(Constants.adminKey)) {
                            throw new Exception("PERMISSIONS ERROR: You can only send event invites to events you are a " +
                                    "member of!");
                        }
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddInvitedEvent(createInviteRequest
                                .to, toItemType, createInviteRequest.about));
                        databaseActionCompiler.add(EventDatabaseActionBuilder.updateAddInvitedMember(createInviteRequest
                                .about, createInviteRequest.to));
                        // Add to the to's receivedInvites
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddReceivedInvite(createInviteRequest.to,
                                toItemType, null, true));
                        break;
                    case challengeInvite:
                        Challenge challenge = Challenge.readChallenge(createInviteRequest.about);
                        if (!challenge.members.contains(fromID) && !fromID.equals(Constants.adminKey)) {
                            throw new Exception("PERMISSIONS ERROR: You can only send challenge invites to challenges you" +
                                    " are a member of!");
                        }
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddInvitedChallenge
                                (createInviteRequest.to, toItemType, createInviteRequest.about));
                        databaseActionCompiler.add(ChallengeDatabaseActionBuilder.updateAddInvitedMember
                                (createInviteRequest.about, createInviteRequest.to));
                        // Add to the to's receivedInvites
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddReceivedInvite(createInviteRequest.to,
                                toItemType, null, true));
                        break;
                    case groupInvite:
                        Group group = Group.readGroup(createInviteRequest.about);
                        if (!group.members.contains(fromID) && !fromID.equals(Constants.adminKey)) {
                            throw new Exception("PERMISSIONS ERROR: You can only send challenge invites to challenges you" +
                                    " are a member of!");
                        }
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddInvitedGroup
                                (createInviteRequest.to, toItemType, createInviteRequest.about));
                        databaseActionCompiler.add(GroupDatabaseActionBuilder.updateAddInvitedMember
                                (createInviteRequest.about, createInviteRequest.to));
                        // Add to the to's receivedInvites
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddReceivedInvite(createInviteRequest.to,
                                toItemType, null, true));
                        break;
                    case eventRequest:
                        // TODO This checking is mostly done in EventDatabaseActionBuilder
                        databaseActionCompiler.add(EventDatabaseActionBuilder.updateAddMemberRequest(createInviteRequest
                                .to, createInviteRequest.about));
                        databaseActionCompiler.add(EventDatabaseActionBuilder.updateAddReceivedInvite
                                (createInviteRequest.to, null, true));
                        break;
                    case challengeRequest:
                        // TODO This checking is mostly done in ChallengeDatabaseActionBuilder?
                        databaseActionCompiler.add(ChallengeDatabaseActionBuilder.updateAddMemberRequest(createInviteRequest
                                .to, createInviteRequest.about));
                        databaseActionCompiler.add(ChallengeDatabaseActionBuilder.updateAddReceivedInvite
                                (createInviteRequest.to, null, true));
                        break;
                    case groupRequest:
                        // TODO This checking is mostly done in GroupDatabaseActionBuilder?
                        databaseActionCompiler.add(GroupDatabaseActionBuilder.updateAddMemberRequest(createInviteRequest
                                .to, createInviteRequest.about));
                        databaseActionCompiler.add(GroupDatabaseActionBuilder.updateAddReceivedInvite
                                (createInviteRequest.to, null, true));
                        break;
                    default:
                        throw new Exception("Forgot to check for or implement invite type = " + createInviteRequest.inviteType);
                }

                // TODO This can be done through the regular Create statement for Users.
                // Send an Ably message! TODO ??
//                JsonObjectBuilder payload = Json.createObjectBuilder()
//                        .add("from", createInviteRequest.from)
//                        .add("to", createInviteRequest.to)
//                        .add("inviteType", createInviteRequest.inviteType)
//                        .add("about", createInviteRequest.about);
//
//                if (createInviteRequest.description != null) {
//                    payload = payload.add("description", createInviteRequest.description);
//                }
//
//                switch (toItemType) {
//                    case "Client":
//                    case "Trainer":
//                    case "Gym":
//                        // Send to the person
//                        databaseActionCompiler.addMessage(createInviteRequest.to + "-Notifications", "Invite", payload);
//                        break;
//                    case "Event":
//                        // Send to the owner
//                        databaseActionCompiler.addMessage(Event.readEvent(createInviteRequest.to).owner + "-Notifications", "Invite", payload);;
//                        break;
//                    case "Challenge":
//                        // Send to the owner
//                        databaseActionCompiler.addMessage(Challenge.readChallenge(createInviteRequest.to).owner + "-Notifications", "Invite", payload);;
//                        break;
//                    case "Group":
//                        // Send to the owners
//                        for (String owner : Group.readGroup(createInviteRequest.to).owners) {
//                            databaseActionCompiler.addMessage(owner + "-Notifications", "Invite", payload);
//                        }
//                        break;
//                    default:
//                        Constants.debugLog("SENDING MESSAGE NOT HANDLED FOR INVITE TO THAT ITEM TYPE = " + toItemType);
//                        break;
//                }

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("createInviteRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createInviteRequest not initialized for CREATE statement!");
        }
    }
}
