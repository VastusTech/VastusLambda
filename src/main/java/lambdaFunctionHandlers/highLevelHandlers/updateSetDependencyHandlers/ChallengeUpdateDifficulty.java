package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Challenge's difficulty.
 */
public class ChallengeUpdateDifficulty {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String difficulty) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Challenge challenge = Challenge.readChallenge(challengeID);
        if (fromID == null || (!fromID.equals(challenge.owner) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update an challenge that you own!");
        }

        // Test the integer
        int diff = Integer.parseInt(difficulty);
        if (diff < 1 || diff > 3) {
            throw new Exception("Difficulty must be 1, 2, or 3!");
        }

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateDifficulty(challengeID, difficulty));

        return databaseActions;
    }
}
