package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreateStreakRequest;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO
 */
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

                boolean ifStreak = false;
                if (createChallengeRequest.challengeType != null) {
                    if (createChallengeRequest.challengeType.equals("streak")) {
                        ifStreak = true;
                        if (createChallengeRequest.streakUpdateSpanType == null ||
                                createChallengeRequest.streakUpdateInterval == null ||
                                createChallengeRequest.streakN == null) {
                            throw new Exception("If the challenge type is a streak, it needs the " +
                                    "streak update span type, the streak update interval and the " +
                                    "streak n to instantiate!");
                        }
                    }
                    else {
                        throw new Exception("Challenge type must be nothing or \"streak\"!");
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

                // If it's a streak challenge, create all the streaks too!
                if (ifStreak && createChallengeRequest.members != null) {
                    for (String member : createChallengeRequest.members) {
                        // Create the Streak for each member in the Challenge!
                        CreateStreakRequest createStreakRequest = new CreateStreakRequest(member,
                                "", "submission", createChallengeRequest.streakUpdateSpanType,
                                createChallengeRequest.streakUpdateInterval, createChallengeRequest.streakN);
                        compilers.addAll(CreateStreak.getCompilers(fromID, createStreakRequest,
                                true));
                    }
                }

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
