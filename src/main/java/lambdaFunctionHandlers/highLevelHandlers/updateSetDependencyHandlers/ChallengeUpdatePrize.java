package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the prize for winning the Challenge.
 */
public class ChallengeUpdatePrize {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String prize) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(Challenge.readChallenge(challengeID).owner) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a challenge that you own!");
        }

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updatePrize(challengeID, prize));

        return databaseActions;
    }
}
