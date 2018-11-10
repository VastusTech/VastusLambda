package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GymAddVacationTimes {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String[] vacationTimes) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(gymID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }

        // Get all the actions for this process
        for (String vacationTime : vacationTimes) {
            // Check the time
            new TimeInterval(vacationTime);
        }

        databaseActions.add(GymDatabaseActionBuilder.updateAddVacationTimes(gymID, vacationTimes));

        return databaseActions;
    }
}
