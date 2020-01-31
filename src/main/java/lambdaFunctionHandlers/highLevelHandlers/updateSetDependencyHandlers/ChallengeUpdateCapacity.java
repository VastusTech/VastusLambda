package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Challenge's capacity to hold a certain number of members.
 */
public class ChallengeUpdateCapacity {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String capacity) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Challenge challenge = Challenge.readChallenge(challengeID);
        if (fromID == null || (!fromID.equals(challenge.owner) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update an challenge that you own!");
        }

        // Test the integer
        int cap = Integer.parseInt(capacity);
        if (!(cap >= 1)) {
            throw new Exception("Capacity must be greater than zero!");
        }

        // TODO Can't set the capacity lower than its current membership?

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateDifficulty(challengeID, capacity));

        return databaseActions;
    }
}
