package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Workout;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers.ClientRemoveFromWorkout;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes a Workout object from the database, from the Clients, from the Trainers, and from the
 * Gym. TODO Also handle refund stuff?
 */
public class DeleteWorkout {
    public static List<DatabaseAction> getActions(String fromID, String workoutID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Workout workout = Workout.readWorkout(workoutID);

        if (fromID == null || (!fromID.equals(workout.trainer) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only delete a workout if you're the trainer!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        // Remove from clients' scheduled workouts and completed workouts
        // Remove clients' scheduled workout times and  completed workout times
        for (String client : workout.clients) {
            // This handles refund data automatically
            databaseActions.addAll(ClientRemoveFromWorkout.getActions(fromID, client, workoutID));
        }

        // remove from trainer's scheduled and completed workouts
        // remove from trainer's scheduled and completed workout times
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledWorkout(workout.trainer, "Trainer",
                workoutID));
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledTime(workout.trainer,
                "Trainer", workout.time.toString()));
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveCompletedWorkout(workout.trainer, "Trainer",
                workoutID));

        // Remove from gym's scheduled and completed workout times
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledWorkout(workout.gym, "Gym", workoutID));
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledTime(workout.gym, "Gym", workout
                .time.toString()));
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveCompletedWorkout(workout.gym, "Gym", workoutID));

        return databaseActions;
    }
}
