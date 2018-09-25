package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class TrainerUpdateWorkoutCapacity {
    public static List<DatabaseAction> getActions(String trainerID, String capacity) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Check to see if it's a valid int
        Integer.parseInt(capacity);

        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updateWorkoutCapacity(trainerID, capacity));

        return databaseActions;
    }
}
