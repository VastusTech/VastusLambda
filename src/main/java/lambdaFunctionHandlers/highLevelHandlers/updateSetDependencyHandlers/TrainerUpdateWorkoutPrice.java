package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Update a Trainer's Workout price.
 */
public class TrainerUpdateWorkoutPrice {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String price) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(trainerID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a trainer you are!");
        }
        // Check to see if the price is an int or not
        Integer.parseInt(price);
        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updateWorkoutPrice(trainerID, price));

        return databaseActions;
    }
}
