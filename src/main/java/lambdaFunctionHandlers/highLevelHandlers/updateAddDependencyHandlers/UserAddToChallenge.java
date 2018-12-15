package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteInvite;

import java.util.ArrayList;
import java.util.List;

public class UserAddToChallenge {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String challengeID)
            throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

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
            if (!fromID.equals(challenge.owner) && !fromID.equals(Constants.adminKey)) {
                throw new Exception("PERMISSIONS ERROR: Only the owner can accept your challenge member request!");
            }
            else {
                ifAcceptingRequest = true;
                databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveMemberRequest(challengeID, userID));
            }
        }
        else {
            if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
                throw new Exception("PERMISSIONS ERROR: Only you can add you to a challenge!");
            }
        }

        // Add to user's challenges
        databaseActions.add(UserDatabaseActionBuilder.updateAddChallenge(userID, itemType,
                challengeID, false));

        // Add to challenge's members
        databaseActions.add(ChallengeDatabaseActionBuilder.updateAddMember(challengeID, userID, ifAcceptingRequest));

        // Delete any potential invites associated with this
        User user = User.readUser(userID, itemType);
        for (String inviteID : user.receivedInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(challengeID)) {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }

        return databaseActions;
    }
}
