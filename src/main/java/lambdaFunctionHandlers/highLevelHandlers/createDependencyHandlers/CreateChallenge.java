package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateChallenge {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateChallengeRequest createChallengeRequest, boolean ifWithCreate) throws Exception {
        if (createChallengeRequest != null) {
            // Create challenge
            if (createChallengeRequest.owner != null && createChallengeRequest.endTime != null && createChallengeRequest
                    .capacity != null && createChallengeRequest.title != null && createChallengeRequest.goal !=
                    null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!fromID.equals(createChallengeRequest.owner) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only create challenges you're going to own!");
                }

                // Check to see if the request features are well formed (i.e not empty string or invalid date)
                new DateTime(createChallengeRequest.endTime);
                if (Integer.parseInt(createChallengeRequest.capacity) <= 0) {
                    throw new Exception("The capacity must be greater than 1!!");
                }

                if (createChallengeRequest.access != null) {
                    if (!createChallengeRequest.access.equals("public") && !createChallengeRequest.access.equals
                            ("private")) {
                        throw new Exception("Create Challenge access must be either \"public\" or \"private\"!");
                    }
                }
                else if (createChallengeRequest.group != null) {
                    createChallengeRequest.access = Group.readGroup(createChallengeRequest.group).access;
                }
                else {
                    throw new Exception("Create Challenge access must either be set or inherit from group!");
                }

                if (createChallengeRequest.restriction != null) {
                    if (!createChallengeRequest.restriction.equals("invite")) {
                        throw new Exception("Create Challenge restriction must be nothing or \"invite\"");
                    }
                }

                if (createChallengeRequest.members == null) {
                    createChallengeRequest.members = new String[]{createChallengeRequest.owner};
                }
                else {
                    ArrayList<String> members = new ArrayList<>(Arrays.asList(createChallengeRequest.members));
                    if (!members.contains(createChallengeRequest.owner)) {
                        members.add(createChallengeRequest.owner);
                        createChallengeRequest.members = members.toArray(new String[]{});
                    }
                }

                databaseActionCompiler.add(ChallengeDatabaseActionBuilder.create(createChallengeRequest, ifWithCreate));

                // Update owners fields
                String ownerItemType = ItemType.getItemType(createChallengeRequest.owner);

                if (ownerItemType.equals("Client") && createChallengeRequest.access.equals("public")) {
                    throw new Exception("Clients cannot create public Challenges!");
                }

                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddOwnedChallenge(createChallengeRequest
                        .owner, ownerItemType, null, true));

                // Update each members fields
                if (createChallengeRequest.members != null) {
                    for (String member : createChallengeRequest.members) {
                        String memberItemType = ItemType.getItemType(member);
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddChallenge
                                (member, memberItemType, null, true));
                    }
                }

                // Add to the challenge's group
                if (createChallengeRequest.group != null) {
                    databaseActionCompiler.add(GroupDatabaseActionBuilder.updateAddChallenge(createChallengeRequest.group,
                            null, true));
                }

                compilers.add(databaseActionCompiler);

                // TODO AUTOMATICALLY CREATE A POST FOR THEM!!!!

                // Manually add a CreatePostRequest, then send it, utilizing the Passover ID functionality!
                CreatePostRequest createPostRequest = new CreatePostRequest(createChallengeRequest.owner,
                        null, "", createChallengeRequest.access, "newChallenge", null, null,
                        createChallengeRequest.group);
                compilers.addAll(CreatePost.getCompilers(fromID, createPostRequest, true));

                return compilers;
            }
            else {
                throw new Exception("createChallengeRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createChallengeRequest not initialized for CREATE statement!");
        }
    }
}
