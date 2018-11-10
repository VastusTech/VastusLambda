package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class TrainerAddAvailableTimes {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String[] availableTimes) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(trainerID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only update a trainer if it's yourself!");
        }

        // Get all the actions for this process
        for (String availableTime : availableTimes) {
            // Check the time
            new TimeInterval(availableTime);
        }

        databaseActions.add(TrainerDatabaseActionBuilder.updateAddAvailableTimes(trainerID, availableTimes));

        return databaseActions;
    }
}
