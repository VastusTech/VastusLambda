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
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(challenge.ownerID, challengeID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(challenge.ownerID, challenge.time.toString()));
        // remove from each member's fields
        for (String memberID : challenge.memberIDs) {
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(memberID, challengeID));
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(memberID, challenge.time.toString()));
        }

        // Delete the challenge
        databaseActions.add(ChallengeDatabaseActionBuilder.delete(challengeID));

        return getActions(challengeID);
    }
}
