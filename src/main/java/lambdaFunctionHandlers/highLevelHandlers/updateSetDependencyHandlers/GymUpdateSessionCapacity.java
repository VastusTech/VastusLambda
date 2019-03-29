package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class GymUpdateSessionCapacity {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String sessionCapacity) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(gymID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }
        // Check to see if the sessionCapacity is valid
        if (Integer.parseInt(sessionCapacity) < 0) {
            throw new Exception("Session capacity must be a positive integer!");
        }

        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateSessionCapacity(gymID, sessionCapacity));

        return databaseActions;
    }
}
