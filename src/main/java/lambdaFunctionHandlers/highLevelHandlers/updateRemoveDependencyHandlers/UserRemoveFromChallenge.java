package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

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

public class UserRemoveFromChallenge {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String challengeID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Challenge challenge = Challenge.readChallenge(challengeID);

        if (!fromID.equals(userID) && !fromID.equals(challenge.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only remove someone if you're the owner or that someone " +
                    "is you!");
        }

        if (!challenge.members.contains(userID)) {
            throw new Exception("You can't remove a user that isn't already in the challenge!");
        }

        if (challenge.owner.equals(userID)) {
            throw new Exception("You can't remove the owner from the challenge! Just delete the challenge in that case!");
        }

        // We delete the challenge from ourselves
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveChallenge(userID, itemType, challengeID));

        // We also remove any invites that pertain to this challenge we may have sent
        User user = User.readUser(userID, itemType);
        for (String inviteID : user.sentInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(challengeID)) {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }

        // And we delete ourselves from the challenge
        databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveMember(challengeID, userID));

        return databaseActions;
    }
}
