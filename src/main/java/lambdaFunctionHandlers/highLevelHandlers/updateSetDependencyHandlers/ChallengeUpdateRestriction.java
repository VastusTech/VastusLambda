package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the restriction level of the Challenge, which forces Users to request joining instead of
 * directly joining the Challenge.
 */
public class ChallengeUpdateRestriction {
    static public List<DatabaseAction> getActions(String fromID, String challengeID, String restriction) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Challenge challenge = Challenge.readChallenge(challengeID);
        if (!fromID.equals(challenge.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update an challenge that you own!");
        }

        if (restriction != null && !restriction.equals("invite")) {
            throw new Exception("restriction value must be either nothing or \"invite\"");
        }

        databaseActions.add(ChallengeDatabaseActionBuilder.updateRestriction(challengeID, restriction));

        return databaseActions;
    }
}
