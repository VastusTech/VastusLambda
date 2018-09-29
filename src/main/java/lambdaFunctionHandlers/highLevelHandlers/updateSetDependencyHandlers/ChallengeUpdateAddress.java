package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ChallengeUpdateAddress {
    public static List<DatabaseAction> getActions(String challengeID, String address) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO Check to see if the address is valid?

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateAddress(challengeID, address));

        return databaseActions;
    }
}
