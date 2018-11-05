package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteChallenge {
    public static List<DatabaseAction> getActions(String challengeID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Challenge challenge = Challenge.readChallenge(challengeID);

        // remove from owner's fields
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveOwnedChallenge(challenge.owner, challengeID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(challenge.owner, challengeID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(challenge.owner, challenge.time.toString()));
        // remove from each member's fields
        for (String member : challenge.members) {
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(member, challengeID));
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(member, challenge.time.toString()));
        }

        // Delete the challenge
        databaseActions.add(ChallengeDatabaseActionBuilder.delete(challengeID));

        return getActions(challengeID);
    }
}
