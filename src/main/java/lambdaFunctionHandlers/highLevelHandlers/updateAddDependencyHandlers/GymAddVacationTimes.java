package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GymAddVacationTimes {
    public static List<DatabaseAction> getActions(String gymID, String[] vacationTimes) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        for (String vacationTime : vacationTimes) {
            // Check the time
            new TimeInterval(vacationTime);

            databaseActions.add(GymDatabaseActionBuilder.updateAddVacationTime(gymID, vacationTime));
        }

        return databaseActions;
    }
}
