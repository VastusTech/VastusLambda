package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class ChallengeAddTag {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String tag) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Challenge challenge = Challenge.readChallenge(challengeID);

        if (!fromID.equals(challenge.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a challenge you own!");
        }

        // TODO Check the tag name?

        databaseActions.add(ChallengeDatabaseActionBuilder.updateAddTag(challengeID, tag));

        return databaseActions;
    }
}
