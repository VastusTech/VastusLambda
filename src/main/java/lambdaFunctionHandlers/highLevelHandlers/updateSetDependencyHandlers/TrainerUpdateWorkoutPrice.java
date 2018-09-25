package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class TrainerUpdateWorkoutPrice {
    public static List<DatabaseAction> getActions(String trainerID, String price) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Check to see if the price is an int or not
        Integer.parseInt(price);
        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updateWorkoutPrice(trainerID, price));

        return databaseActions;
    }
}
