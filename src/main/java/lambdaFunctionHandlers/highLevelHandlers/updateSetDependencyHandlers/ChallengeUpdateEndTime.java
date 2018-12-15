package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ChallengeUpdateEndTime {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String endTime) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(Challenge.readChallenge(challengeID).owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a challenge that you own!");
        }

        if (endTime == null) {
            throw new Exception("Must have a real endTime to update the challenge with!");
        }

        // Check the time
        new DateTime(endTime);

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateEndTime(challengeID, endTime));

        return databaseActions;
    }
}
