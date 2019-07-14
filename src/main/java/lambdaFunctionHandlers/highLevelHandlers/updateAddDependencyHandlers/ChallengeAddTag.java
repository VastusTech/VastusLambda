package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a String tag to a Challenge indicating what kind of a Challenge it is.
 */
public class ChallengeAddTag {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String tag) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Challenge challenge = Challenge.readChallenge(challengeID);

        if (fromID == null || (!fromID.equals(challenge.owner) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a challenge you own!");
        }

        // TODO Check the tag name?

        databaseActions.add(ChallengeDatabaseActionBuilder.updateAddTag(challengeID, tag));

        return databaseActions;
    }
}
