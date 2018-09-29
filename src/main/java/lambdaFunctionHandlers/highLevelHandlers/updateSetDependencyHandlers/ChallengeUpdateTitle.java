package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ChallengeUpdateTitle {
    public static List<DatabaseAction> getActions(String challengeID, String title) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateTitle(challengeID, title));

        return databaseActions;
    }
}
