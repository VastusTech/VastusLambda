package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Challenge's end time.
 */
public class ChallengeUpdateEndTime {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String endTime) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(Challenge.readChallenge(challengeID).owner) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a challenge that you own!");
        }

        if (endTime == null) {
            throw new Exception("Must have a real endTime to update the challenge with!");
        }

        // Check the time
        new DateTime(endTime);

        // TODO Make sure that it isn't past right now?

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateEndTime(challengeID, endTime));

        return databaseActions;
    }
}
