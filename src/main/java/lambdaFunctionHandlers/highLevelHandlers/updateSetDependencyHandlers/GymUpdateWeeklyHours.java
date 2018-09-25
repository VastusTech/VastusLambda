package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GymUpdateWeeklyHours {
    public static List<DatabaseAction> getActions(String gymID, String[] weeklyHours) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO Check if it interferes with current workouts
        // TODO Check that all the times are well formed
        // TODO THIS SHOULD BE ADDRESSED VERY SOON
        // TODO We probably need to cancel any workouts that
        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateWeeklyHours(gymID, weeklyHours));


        return databaseActions;
    }
}
