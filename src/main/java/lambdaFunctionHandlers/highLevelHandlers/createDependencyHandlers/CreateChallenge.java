package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseObjects.User;
import main.java.databaseOperations.exceptions.PermissionsException;
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
import java.util.Map;

/**
 * Creates a Challenge in the database, checks the inputs, adds the Challenge to the members, to the
 * owner, creates {@link main.java.databaseObjects.Streak}s for relevant Challenge, adds to the
 * group (if applicable), and automatically creates a {@link main.java.databaseObjects.Post} for the
 * owner.
 */
public class CreateChallenge {
    /**
     * Gets the list of {@link DatabaseActionCompiler}s representing the overall transaction for the
     * operation.
     *
     * @param fromID The ID of the User invoking the Lambda request.
     * @param createChallengeRequest The {@link CreateChallengeRequest} representing the creation of
     *                               the {@link main.java.databaseObjects.Challenge}.
     * @param depth The current depth for the transaction. AKA how many compilers came directly
     *              before it.
     * @return The list of {@link DatabaseActionCompiler}s representing the overall transaction.
     * @throws Exception If anything goes wrong... TODO bad answer
     */
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateChallengeRequest createChallengeRequest, int depth) throws Exception {
        if (createChallengeRequest != null) {
            // Create challenge
            if (createChallengeRequest.owner != null && createChallengeRequest.endTime != null && createChallengeRequest
                    .capacity != null && createChallengeRequest.title != null && createChallengeRequest.goal !=
                    null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // TODO This needs to be a more foolproof system for getting a completely unique
                // TODO identifier
                databaseActionCompiler.setPassoverIdentifier("Challenge" + Integer.toString(depth));

                if (fromID == null || (!fromID.equals(createChallengeRequest.owner) && !Constants.isAdmin(fromID))) {
                    throw new PermissionsException("You can only create challenges you're going to own!");
                }

                // Check to see if the request features are well formed (i.e not empty string or invalid date)
                new DateTime(createChallengeRequest.endTime);
                if (Integer.parseInt(createChallengeRequest.capacity) <= 0) {
                    throw new Exception("The capacity must be greater than 1!!");
                }
                if (createChallengeRequest.difficulty != null) {
                    int difficulty = Integer.parseInt(createChallengeRequest.difficulty);
                    if (difficulty < 1 || difficulty > 3) {
                        throw new Exception("Difficulty must be 1, 2, or 3!");
                    }
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
                else if (createChallengeRequest.streakN != null || createChallengeRequest
                            .streakUpdateSpanType != null || createChallengeRequest
                            .streakUpdateInterval != null) {
                    throw new Exception("Non-streak Challenge cannot have streak attributes like " +
                            "\"streakN\", \"streakUpdateSpanType\", or \"streakUpdateInterval\"");
                }

                if (depth == 0) {
                    databaseActionCompiler.add(ChallengeDatabaseActionBuilder.create(createChallengeRequest, null));
                }
                else {
                    // TODO If we ever try to create Challenges automatically, figure out which
                    // TODO attributes need which passover Identifiers.
                    databaseActionCompiler.add(ChallengeDatabaseActionBuilder.create(createChallengeRequest, null));
                }

                // Update owners fields
                String ownerItemType = ItemType.getItemType(createChallengeRequest.owner);

                if (ownerItemType.equals("Client") && createChallengeRequest.access.equals("public")) {
                    throw new Exception("Clients cannot create public Challenges!");
                }

                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddOwnedChallenge(createChallengeRequest
                        .owner, ownerItemType, null, true));

                // Update each members fields
                if (createChallengeRequest.members != null) {
                    User owner = User.readUser(createChallengeRequest.owner);
                    for (String member : createChallengeRequest.members) {
                        if (!(member.equals(createChallengeRequest.owner) || owner.friends.contains(member))) {
                            throw new Exception("Existing members contains a user not in the owner's friends");
                        }
                        String memberItemType = ItemType.getItemType(member);
                        databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddChallenge
                                (member, memberItemType, null, true));
                    }
                }

                // Add to the challenge's group
                if (createChallengeRequest.group != null) {
                    if (!Group.readGroup(createChallengeRequest.group).owners.contains(createChallengeRequest.owner)) {
                        throw new Exception("Group Challenge owner must also be an owner of the Group");
                    }
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
                                depth + 1, databaseActionCompiler.getPassoverIdentifier()));
                    }
                }

                // Manually add a CreatePostRequest, then send it, utilizing the Passover ID functionality!
                CreatePostRequest createPostRequest = new CreatePostRequest(createChallengeRequest.owner,
                        createChallengeRequest.owner + " created a new challenge", "",
                        createChallengeRequest.access, "newChallenge", null, null,
                        createChallengeRequest.group);
                compilers.addAll(CreatePost.getCompilers(fromID, createPostRequest, depth + 1,
                        databaseActionCompiler.getPassoverIdentifier()));

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
