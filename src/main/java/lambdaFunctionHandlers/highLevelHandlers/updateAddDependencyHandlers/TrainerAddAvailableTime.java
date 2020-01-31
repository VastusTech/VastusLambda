package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds an available time interval to a Trainer, indicating that Clients can schedule workouts with
 * them during that time. TODO Revisit
 */
public class TrainerAddAvailableTime {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String availableTime) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(trainerID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a trainer if it's yourself!");
        }

        // Check the time
        new TimeInterval(availableTime);

        databaseActions.add(TrainerDatabaseActionBuilder.updateAddAvailableTime(trainerID, availableTime));

        return databaseActions;
    }
}
