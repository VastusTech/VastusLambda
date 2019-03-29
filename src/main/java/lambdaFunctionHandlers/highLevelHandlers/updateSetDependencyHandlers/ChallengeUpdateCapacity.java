package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class ChallengeUpdateCapacity {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String capacity) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Challenge challenge = Challenge.readChallenge(challengeID);
        if (!fromID.equals(challenge.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update an challenge that you own!");
        }

        // Test the integer
        int cap = Integer.parseInt(capacity);
        if (!(cap >= 1)) {
            throw new Exception("Capacity must be greater than zero!");
        }

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateDifficulty(challengeID, capacity));

        return databaseActions;
    }
}
