package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Update a Trainer's Workout sticker, which indicates which workout they prefer doing.
 */
public class TrainerUpdateWorkoutSticker {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String sticker) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(trainerID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a trainer you are!");
        }
        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updateWorkoutSticker(trainerID, sticker));

        return databaseActions;
    }
}
