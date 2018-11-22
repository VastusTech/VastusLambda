package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GymUpdateWeeklyHours {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String[] weeklyHours) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(gymID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }
        // TODO Check if it interferes with current workouts
        // TODO Check that all the times are well formed
        // TODO THIS SHOULD BE ADDRESSED VERY SOON
        // TODO We probably need to cancel any workouts that
        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateWeeklyHours(gymID, weeklyHours));


        return databaseActions;
    }
}
