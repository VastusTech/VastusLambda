package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
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

        //Constants.debugLog("Getting delete challenge actions");

        // TODO This is ripe for abuse...

        // remove from owner's fields
        //Constants.debugLog("Removing from owner's owned challenges field");
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveOwnedChallenge(challenge.owner, challengeID));
        //Constants.debugLog("Removing from owner's scheduled challenges");
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(challenge.owner, challengeID));
        //Constants.debugLog("Removing from the owner's scheduled times");
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(challenge.owner, challenge.time.toString()));
        // remove from each member's fields
        //Constants.debugLog("Removing from all the members scheduled challenges: members size = " + challenge.members.size());
        for (String member : challenge.members) {
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(member, challengeID));
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(member, challenge.time.toString()));
        }

        // Delete the challenge
        //Constants.debugLog("Deleting the challenge");
        databaseActions.add(ChallengeDatabaseActionBuilder.delete(challengeID));

        //Constants.debugLog("Returning the final actions");
        return databaseActions;
    }
}
