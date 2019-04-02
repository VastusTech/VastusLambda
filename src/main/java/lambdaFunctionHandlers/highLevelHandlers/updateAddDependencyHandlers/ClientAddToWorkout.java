package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Workout;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.WorkoutDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a Client to a scheduled workout and updates all the fields involved in a Client joining the
 * workout (like missing reviews). TODO Is this applicable in the app still?
 */
public class ClientAddToWorkout {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String workoutID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(clientID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only add yourself to a workout!");
        }

        // Get all the actions for this process
        Workout workout = Workout.readWorkout(workoutID);
        // Add to client's scheduled workouts
        databaseActions.add(UserDatabaseActionBuilder.updateAddScheduledWorkout(clientID, "Client",
                workoutID, false));
        // Add to client's scheduled workout times
        databaseActions.add(UserDatabaseActionBuilder.updateAddScheduledTime(clientID, "Client", workout.time.toString
                (), null));
        // Add to workout's clients
        databaseActions.add(WorkoutDatabaseActionBuilder.updateAddClient(workoutID, clientID));
        // Add to workout's missingReviews
        databaseActions.add(WorkoutDatabaseActionBuilder.updateAddMissingReview
                (workoutID, clientID));

        return databaseActions;
    }
}
