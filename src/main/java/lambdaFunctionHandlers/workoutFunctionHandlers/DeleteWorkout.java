package main.java.lambdaFunctionHandlers.workoutFunctionHandlers;

import main.java.databaseObjects.Client;
import main.java.databaseObjects.Workout;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.clientFunctionHandlers.ClientRemoveFromWorkout;

import java.util.ArrayList;
import java.util.List;

public class DeleteWorkout {
    public static List<DatabaseAction> getActions(String workoutID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Workout workout = Workout.readWorkout(workoutID);

        // Remove from clients' scheduled workouts and completed workouts
        // Remove clients' scheduled workout times and  completed workout times
        for (String clientID : workout.clientIDs) {
            // This handles refund data automatically
            databaseActions.addAll(ClientRemoveFromWorkout.getActions(clientID, workoutID));
        }

        // remove from trainer's scheduled and completed workouts
        // remove from trainer's scheduled and completed workout times
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveScheduledWorkout(workout.trainerID, workoutID));
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveScheduledWorkoutTime(workout.trainerID, workout
                .time.toString()));
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveCompletedWorkout(workout.trainerID, workoutID));
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveCompletedWorkoutTime(workout.trainerID, workout
                .time.toString()));

        // Remove from gym's scheduled and completed workout times
        databaseActions.add(GymDatabaseActionBuilder.updateRemoveScheduledWorkout(workout.gymID, workoutID));
        databaseActions.add(GymDatabaseActionBuilder.updateRemoveScheduledWorkoutTime(workout.gymID, workout
                .time.toString()));
        databaseActions.add(GymDatabaseActionBuilder.updateRemoveCompletedWorkout(workout.gymID, workoutID));
        databaseActions.add(GymDatabaseActionBuilder.updateRemoveCompletedWorkoutTime(workout.gymID, workout
                .time.toString()));

        return databaseActions;
    }
}
