package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Client;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ChallengeUpdateWinner {
    public static List<DatabaseAction> getActions(String challengeID, String winner) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Challenge challenge = Challenge.readChallenge(challengeID);

        if (!challenge.members.contains(winner)) {
            throw new Exception("Winner must be a member of the challenge!");
        }

        if (!challenge.time.hasAlreadyFinished()) {
            throw new Exception("You cannot complete the challenge if it hasn't already finished!");
        }

        // Set the winner and update their challenges won
        databaseActions.add(ChallengeDatabaseActionBuilder.updateWinner(challengeID, winner));

        Client winnerClient = Client.readClient(winner);
        databaseActions.add(ClientDatabaseActionBuilder.updateChallengesWon(winner, Integer.toString(winnerClient
                .challengesWon + 1)));

        //  Finally remove the challenge from everyone's scheduled and into their completed
        for (String clientID : challenge.members) {
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(clientID, challengeID));
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(clientID, challenge.time.toString()));
            databaseActions.add(ClientDatabaseActionBuilder.updateAddCompletedChallenge(clientID, challengeID));
        }

        return databaseActions;
    }
}
