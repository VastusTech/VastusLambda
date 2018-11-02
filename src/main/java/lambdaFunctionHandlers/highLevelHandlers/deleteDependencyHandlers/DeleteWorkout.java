package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Workout;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers.ClientRemoveFromWorkout;

import java.util.ArrayList;
import java.util.List;

public class DeleteWorkout {
    public static List<DatabaseAction> getActions(String workoutID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Workout workout = Workout.readWorkout(workoutID);

        // Remove from clients' scheduled workouts and completed workouts
        // Remove clients' scheduled workout times and  completed workout times
        for (String client : workout.clients) {
            // This handles refund data automatically
            databaseActions.addAll(ClientRemoveFromWorkout.getActions(client, workoutID));
        }

        // remove from trainer's scheduled and completed workouts
        // remove from trainer's scheduled and completed workout times
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveScheduledWorkout(workout.trainer, workoutID));
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveScheduledTime(workout.trainer, workout
                .time.toString()));
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveCompletedWorkout(workout.trainer, workoutID));

        // Remove from gym's scheduled and completed workout times
        databaseActions.add(GymDatabaseActionBuilder.updateRemoveScheduledWorkout(workout.gym, workoutID));
        databaseActions.add(GymDatabaseActionBuilder.updateRemoveScheduledTime(workout.gym, workout
                .time.toString()));
        databaseActions.add(GymDatabaseActionBuilder.updateRemoveCompletedWorkout(workout.gym, workoutID));

        return databaseActions;
    }
}
