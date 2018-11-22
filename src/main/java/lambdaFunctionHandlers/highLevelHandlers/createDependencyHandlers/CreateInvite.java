package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.InviteDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateInviteRequest;

public class CreateInvite {
    public static String handle(String fromID, CreateInviteRequest createInviteRequest) throws Exception {
        if (createInviteRequest != null) {
            // Create client
            if (createInviteRequest.from != null && createInviteRequest.to != null && createInviteRequest
                    .inviteType != null && createInviteRequest.about != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!(fromID.equals(createInviteRequest.from)) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only send invites you have created!");
                }

                // Check to see if the request features are well formed (i.e not invalid date or time)
                if (!createInviteRequest.inviteType.equals("friendRequest") && !createInviteRequest.inviteType.equals
                        ("eventInvite")) {
                    throw new Exception("Did not recognize invite type = " + createInviteRequest.inviteType + "!!");
                }

                if (createInviteRequest.to.equals(createInviteRequest.from)) {
                    throw new Exception("Can't send an invite to yourself!");
                }

                // Add the create statement
                databaseActionCompiler.add(InviteDatabaseActionBuilder.create(createInviteRequest));

                // Then we add it to the object's stuff
                // Add to the from's sentInvites
                String fromItemType = ItemType.getItemType(createInviteRequest.from);
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddSentInvite(createInviteRequest.from,
                        fromItemType, null, true));

                // Add to the to's receivedInvites
                String toItemType = ItemType.getItemType(createInviteRequest.to);
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddReceivedInvite(createInviteRequest.to,
                        toItemType, null, true));

                if (createInviteRequest.inviteType.equals("friendRequest")) {
                    if (!(fromID.equals(createInviteRequest.about)) && !fromID.equals(Constants.adminKey)) {
                        throw new Exception("PERMISSIONS ERROR: You can only send friend requests for yourself!");
                    }
                    databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddFriendRequest(createInviteRequest
                            .to, toItemType, createInviteRequest.about));
                }
                else if (createInviteRequest.inviteType.equals("eventInvite")) {
                    Event event = Event.readEvent(createInviteRequest.about);
                    if (!event.members.contains(fromID) && !fromID.equals(Constants.adminKey)) {
                        throw new Exception("PERMISSIONS ERROR: You can only send event invites to events you are a " +
                                "member of!");
                    }
                    databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddInvitedEvent(createInviteRequest
                            .to, toItemType, createInviteRequest.about));
                    databaseActionCompiler.add(EventDatabaseActionBuilder.updateAddInvitedMember(createInviteRequest
                            .about, createInviteRequest.to));
                }

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
            }
            else {
                throw new Exception("createClientRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createInviteRequest not initialized for CREATE statement!");
        }
    }
}
