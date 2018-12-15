package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ChallengeUpdateDescription {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String description) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(Challenge.readChallenge(challengeID).owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a challenge that you own!");
        }

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateDescription(challengeID, description));

        return databaseActions;
    }
}