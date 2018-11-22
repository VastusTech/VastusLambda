package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Workout;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers.ClientRemoveFromWorkout;

import java.util.ArrayList;
import java.util.List;

public class DeleteWorkout {
    public static List<DatabaseAction> getActions(String fromID, String workoutID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Workout workout = Workout.readWorkout(workoutID);

        if (!fromID.equals(workout.trainer) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a workout if you're the trainer!");
        }

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
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledTime(workout.trainer, "Trainer", workout
                .time.toString()));
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
