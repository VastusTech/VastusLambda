package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteStreak;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Challenge's winner, completing the Challenge, and finishing all the Streaks involved.
 */
public class ChallengeUpdateWinner {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String winner) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Challenge challenge = Challenge.readChallenge(challengeID);

        if (fromID == null || (!challenge.owner.equals(fromID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("In order to update an challenge, you need to be the owner!");
        }

        String winnerItemType = ItemType.getItemType(winner);

        // Set the winner and update their challenges won
        databaseActions.add(ChallengeDatabaseActionBuilder.updateWinner(challengeID, winner));
        databaseActions.add(UserDatabaseActionBuilder.updateAddChallengeWon(winner, winnerItemType, challengeID));

        //  Finally remove the challenge from everyone's scheduled and into their completed
        for (String userID : challenge.members) {
            String userItemType = ItemType.getItemType(userID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveChallenge(userID, userItemType, challengeID));
            databaseActions.add(UserDatabaseActionBuilder.updateAddCompletedChallenge(userID, userItemType, challengeID));
        }

        // Updated Completed to Group as well
        if (challenge.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveChallenge(challenge.group, challengeID));
            databaseActions.add(GroupDatabaseActionBuilder.updateAddCompletedChallenge(challenge.group, challengeID));
        }

        // Set the challenge if completed to true
        databaseActions.add(ChallengeDatabaseActionBuilder.updateIfCompleted(challengeID, "true"));

        // Delete all the streaks as well!
        for (String streakID : challenge.streaks) {
            databaseActions.addAll(DeleteStreak.getActions(fromID, streakID));
        }

        return databaseActions;
    }
}
