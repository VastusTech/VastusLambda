package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class TrainerRemoveAvailableTime {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String availableTime) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(trainerID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a trainer if that trainer is yourself!");
        }

        // Check the time
        new TimeInterval(availableTime);

        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveAvailableTime(trainerID, availableTime));

        return databaseActions;
    }
}
