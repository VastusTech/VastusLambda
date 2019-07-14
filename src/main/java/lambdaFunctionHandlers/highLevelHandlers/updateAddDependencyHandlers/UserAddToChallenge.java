package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateStreak;
import main.java.lambdaFunctionHandlers.requestObjects.CreateStreakRequest;
import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteInvite;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a User to a Challenge, checking the validity, deleting any Invites associated with them, and
 * creates a Streak for the User (if applicable).
 */
public class UserAddToChallenge {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, String userID, String itemType, String challengeID)
            throws Exception {
        List<DatabaseActionCompiler> compilers = new ArrayList<>();
        DatabaseActionCompiler compiler = new DatabaseActionCompiler();

        // Get all the actions for this process
        Challenge challenge = Challenge.readChallenge(challengeID);

        // Make sure they aren't already inside the challenge
        if (challenge.members.contains(userID)) {
            throw new Exception("That user is already in the challenge!");
        }

        if (challenge.ifCompleted) {
            throw new Exception("That challenge is already completed!");
        }

        boolean ifAcceptingRequest = false;

        if (challenge.memberRequests.contains(userID)) {
            if (fromID == null || (!fromID.equals(challenge.owner) && !Constants.isAdmin(fromID))) {
                throw new PermissionsException("Only the owner can accept your challenge member request!");
            }
            else {
                ifAcceptingRequest = true;
//                databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveMemberRequest(challengeID, userID));
            }
        }
        else {
            if (fromID == null || (!fromID.equals(userID) && !Constants.isAdmin(fromID))) {
                throw new PermissionsException("Only you can add you to a challenge!");
            }
        }

        if (challenge.group != null) {
            if (!Group.readGroup(challenge.group).members.contains(userID)) {
                throw new Exception("Cannot join a Group Challenge if you aren't a part of the Group!");
            }
        }

        // Add to user's challenges
        compiler.add(UserDatabaseActionBuilder.updateAddChallenge(userID, itemType,
                challengeID, false));

        // Add to challenge's members
        compiler.add(ChallengeDatabaseActionBuilder.updateAddMember(challengeID, userID, ifAcceptingRequest));

        // Delete any potential invites associated with this
        User user = User.readUser(userID, itemType);
        for (String inviteID : user.receivedInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(challengeID)) {
                compiler.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }
        for (String inviteID : challenge.receivedInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(userID)) {
                compiler.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }

        compilers.add(compiler);

        // If it's a streak challenge, create the streak too!
        if (challenge.challengeType != null && challenge.challengeType.equals("streak")) {
            compilers.addAll(CreateStreak.getCompilers(fromID,
                    new CreateStreakRequest(userID, challengeID, "submission",
                            challenge.streakUpdateSpanType, challenge.streakUpdateInterval,
                            challenge.streakN), 0, null));
        }

        return compilers;
    }
}
