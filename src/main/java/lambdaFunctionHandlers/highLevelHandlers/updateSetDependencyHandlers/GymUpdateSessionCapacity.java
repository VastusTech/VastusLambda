package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GymUpdateSessionCapacity {
    public static List<DatabaseAction> getActions(String gymID, String sessionCapacity) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Check to see if the sessionCapacity is valid
        if (Integer.parseInt(sessionCapacity) < 0) {
            throw new Exception("Session capacity must be a positive integer!");
        }

        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateSessionCapacity(gymID, sessionCapacity));

        return databaseActions;
    }
}
