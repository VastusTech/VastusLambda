package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class TrainerUpdatePreferredIntensity {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String intensity) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(trainerID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a trainer you are!");
        }
        int intIntensity = Integer.parseInt(intensity);
        if (intIntensity < 1 || intIntensity > 3) {
            throw new Exception("Intensity must be \"1\", \"2\", or \"3\"!");
        }
        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updatePreferredIntensity(trainerID, intensity));

        return databaseActions;
    }
}
