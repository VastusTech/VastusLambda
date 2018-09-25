package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class TrainerUpdateWorkoutSticker {
    public static List<DatabaseAction> getActions(String trainerID, String sticker) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updateWorkoutSticker(trainerID, sticker));

        return databaseActions;
    }
}
