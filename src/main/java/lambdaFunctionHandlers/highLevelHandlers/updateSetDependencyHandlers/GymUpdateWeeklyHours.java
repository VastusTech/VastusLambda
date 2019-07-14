package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the Gym's weekly hours, indicating when the Gym is open for business and workouts.
 */
public class GymUpdateWeeklyHours {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String[] weeklyHours) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(gymID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a gym you own!");
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
