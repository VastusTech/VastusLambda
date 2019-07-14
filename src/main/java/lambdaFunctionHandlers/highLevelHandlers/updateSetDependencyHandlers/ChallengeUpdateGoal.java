package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Challenge's main goal.
 */
public class ChallengeUpdateGoal {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String goal) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(Challenge.readChallenge(challengeID).owner) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a challenge that you own!");
        }

        if (goal == null) {
            throw new Exception("Must have a real goal to update the challenge with!");
        }

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateGoal(challengeID, goal));

        return databaseActions;
    }
}
