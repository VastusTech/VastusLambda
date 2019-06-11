package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.logic.Constants;

public class ChallengeUpdatePrizeImagePath {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String prizeImagePath) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(Challenge.readChallenge(challengeID).owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a challenge that you own!");
        }

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updatePrizeImagePath(challengeID, prizeImagePath));

        return databaseActions;
    }
}
