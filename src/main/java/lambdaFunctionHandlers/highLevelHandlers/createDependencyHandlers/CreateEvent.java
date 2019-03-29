package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateEventRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO
 */
public class CreateEvent {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateEventRequest createEventRequest, boolean ifWithCreate) throws Exception {
        if (createEventRequest != null) {
            // Create event
            if (createEventRequest.owner != null && createEventRequest.time != null && createEventRequest
                    .capacity != null && createEventRequest.address != null && createEventRequest.title !=
                    null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!fromID.equals(createEventRequest.owner) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only create events you're going to own!");
                }

                // Check to see if the request features are well formed (i.e not empty string or invalid date)
                new TimeInterval(createEventRequest.time);
                if (Integer.parseInt(createEventRequest.capacity) < 1) {
                    throw new Exception("The capacity must be greater than 1!!");
                }

                if (createEventRequest.access != null) {
                    if (!createEventRequest.access.equals("public") && !createEventRequest.access.equals
                            ("private")) {
                        throw new Exception("Create Event access must be either \"public\" or \"private\"!");
                    }
                }
                else if (createEventRequest.group != null && createEventRequest.challenge != null) {
                        throw new Exception("An event can only be a part of either a Group or a" +
                                " Challenge, not both!");
                }
                else if (createEventRequest.group != null) {
                    createEventRequest.access = Group.readGroup(createEventRequest.group).access;
                }
                else if (createEventRequest.challenge != null) {
                    createEventRequest.access = Challenge.readChallenge(createEventRequest.challenge).access;
                }
                else {
                    throw new Exception("Create Event access must either be set or inherit from a " +
                            "Challenge or a Group!");
                }

                if (createEventRequest.restriction != null) {
                    if (!createEventRequest.restriction.equals("invite")) {
                        throw new Exception("Create Event restriction must be nothing or \"invite\"");
                    }
                }

                if (createEventRequest.members == null) {
                    createEventRequest.members = new String[]{createEventRequest.owner};
                }
                else {
                    ArrayList<String> members = new ArrayList<>(Arrays.asList(createEventRequest.members));
                    if (!members.contains(createEventRequest.owner)) {
                        members.add(createEventRequest.owner);
                        createEventRequest.members = members.toArray(new String[]{});
                    }
                }

                databaseActionCompiler.add(EventDatabaseActionBuilder.create(createEventRequest, ifWithCreate));

                // Update owners fields
                String ownerItemType = ItemType.getItemType(createEventRequest.owner);

                if (ownerItemType.equals("Client") && createEventRequest.access.equals("public")) {
                    throw new Exception("Clients cannot create public Events!");
                }

                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddOwnedEvent(createEventRequest
                        .owner, ownerItemType, null, true));

                // Update each members fields
                if (createEventRequest.members != null) {
                    for (String member : createEventRequest.members) {
                        String memberItemType = ItemType.getItemType(member);
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddScheduledEvent
                                (member, memberItemType, null, true));
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddScheduledTime
                                (member, memberItemType, createEventRequest.time, null));
                    }
                }

                // Update the challenge's field
                if (createEventRequest.challenge != null) {
                    Challenge challenge = Challenge.readChallenge(createEventRequest.challenge);
                    if (!fromID.equals(challenge.owner) && !fromID.equals(Constants.adminKey)) {
                        throw new Exception("PERMISSIONS ERROR: You can only add an event to a challenge if you own " +
                                "that challenge!");
                    }
                    databaseActionCompiler.add(ChallengeDatabaseActionBuilder.updateAddEvent(createEventRequest
                            .challenge, null, true));
                }

                // Add to the event's group
                if (createEventRequest.group != null) {
                    databaseActionCompiler.add(GroupDatabaseActionBuilder.updateAddEvent(createEventRequest.group,
                            null, true));
                }

                compilers.add(databaseActionCompiler);

                // TODO Automatically create a post for them!

                return compilers;
            }
            else {
                throw new Exception("createEventRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createEventRequest not initialized for CREATE statement!");
        }
    }
}
