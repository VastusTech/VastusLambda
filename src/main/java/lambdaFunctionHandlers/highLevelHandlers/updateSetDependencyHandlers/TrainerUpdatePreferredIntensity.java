package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class TrainerUpdatePreferredIntensity {
    public static List<DatabaseAction> getActions(String trainerID, String intensity) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        int intIntensity = Integer.parseInt(intensity);
        if (intIntensity < 1 || intIntensity > 3) {
            throw new Exception("Intensity must be \"1\", \"2\", or \"3\"!");
        }
        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updatePreferredIntensity(trainerID, intensity));

        return databaseActions;
    }
}
