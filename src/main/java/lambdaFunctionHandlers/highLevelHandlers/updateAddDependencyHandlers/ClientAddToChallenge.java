package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientAddToChallenge {
    public static List<DatabaseAction> getActions(String clientID, String challengeID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Challenge challenge = Challenge.readChallenge(challengeID);

        // Add to client's scheduled workouts
        databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledChallenge(clientID,
                challengeID, false));
        // Add to client's scheduled workout times
        databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledTime(clientID, challenge.time.toString
                ()));
        // Add to workout's clients
        databaseActions.add(ChallengeDatabaseActionBuilder.updateAddMember(challengeID, clientID));

        return databaseActions;
    }
}
