package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseObjects.Client;
import main.java.databaseObjects.Workout;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.WorkoutDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientRemoveFromWorkout {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String workoutID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Workout workout = Workout.readWorkout(workoutID);
        Client client = Client.readClient(clientID);

        if (!fromID.equals(clientID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only remove yourself from a workout!");
        }

        // We delete the workout from ourselves
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledWorkout(clientID, workoutID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(clientID, workout.time
                .toString()));

        // And we delete ourselves from the workout
        databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveClient(workoutID, clientID));
        databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveMissingReview(workoutID, clientID, false));

        // Then we delete the workout only if it is empty
        databaseActions.add(WorkoutDatabaseActionBuilder.deleteIfEmpty(workoutID));

        // If this was a scheduled workout, we would need to be refunded for it
        if (client.scheduledWorkouts.contains(workoutID)) {
            // TODO THIS IS WHERE REFUNDS WOULD GO (As a process that goes after the successful completion?)
        }

        return databaseActions;
    }
}
