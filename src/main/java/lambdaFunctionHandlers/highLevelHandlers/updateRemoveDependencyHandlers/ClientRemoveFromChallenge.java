package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientRemoveFromChallenge {
    public static List<DatabaseAction> getActions(String clientID, String challengeID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Challenge challenge = Challenge.readChallenge(challengeID);

        // We delete the party from ourselves
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(clientID, challengeID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(clientID, challenge.time
                .toString()));

        // And we delete ourselves from the challenge
        databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveMember(challengeID, clientID));

        return databaseActions;
    }
}
