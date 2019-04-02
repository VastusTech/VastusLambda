package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Update a trainer's Workout capacity (how many people they can handle in one workout).
 */
public class TrainerUpdateWorkoutCapacity {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String capacity) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(trainerID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a trainer you are!");
        }
        // Check to see if it's a valid int
        Integer.parseInt(capacity);

        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updateWorkoutCapacity(trainerID, capacity));

        return databaseActions;
    }
}
