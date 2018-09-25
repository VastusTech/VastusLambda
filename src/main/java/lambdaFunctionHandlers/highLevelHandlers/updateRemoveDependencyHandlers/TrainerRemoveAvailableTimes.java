package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class TrainerRemoveAvailableTimes {
    public static List<DatabaseAction> getActions(String trainerID, String[] availableTimes) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO Get all the actions for this process
        for (String availableTime : availableTimes) {
            // Check the time
            new TimeInterval(availableTime);

            databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveAvailableTime(trainerID, availableTime));
        }

        return databaseActions;
    }
}
