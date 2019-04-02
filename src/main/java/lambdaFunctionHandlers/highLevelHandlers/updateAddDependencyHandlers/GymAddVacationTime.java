package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a designated vacation time to a gym, saying that there can be no workouts during that time.
 */
public class GymAddVacationTime {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String vacationTime) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(gymID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }

        // Check the time
        new TimeInterval(vacationTime);

        databaseActions.add(GymDatabaseActionBuilder.updateAddVacationTime(gymID, vacationTime));

        return databaseActions;
    }
}
