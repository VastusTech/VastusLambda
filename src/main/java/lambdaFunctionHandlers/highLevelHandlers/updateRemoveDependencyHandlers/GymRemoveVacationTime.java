package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Removes a vacation time interval from a Gym.
 */
public class GymRemoveVacationTime {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String vacationTime) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(gymID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a gym you own!");
        }

        // Check the time
        new TimeInterval(vacationTime);

        databaseActions.add(GymDatabaseActionBuilder.updateRemoveVacationTime(gymID, vacationTime));

        return databaseActions;
    }
}
