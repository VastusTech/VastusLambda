package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ChallengeUpdateWinner {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String winner) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Challenge challenge = Challenge.readChallenge(challengeID);

        if (!challenge.owner.equals(fromID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: In order to update an challenge, you need to be the owner!");
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

        // Set the event if completed to true
        databaseActions.add(ChallengeDatabaseActionBuilder.updateIfCompleted(challengeID, "true"));

        return databaseActions;
    }
}
