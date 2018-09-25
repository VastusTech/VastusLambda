package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.WorkoutDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateWorkoutRequest;

import java.util.ArrayList;
import java.util.List;

public class CreateWorkout {
    public static String handle(CreateWorkoutRequest createWorkoutRequest) throws Exception {
        if (createWorkoutRequest != null) {
            if (createWorkoutRequest.time != null && createWorkoutRequest.trainerID != null && createWorkoutRequest
                    .clientIDs != null && createWorkoutRequest.gymID != null && createWorkoutRequest.capacity != null
                    && createWorkoutRequest.sticker != null && createWorkoutRequest.intensity != null &&
                    createWorkoutRequest.price != null) {

                List<DatabaseAction> databaseActions = new ArrayList<>();

                // TODO Check to see if the request features are well formed (i.e not empty string or invalid date)

                // Create Workout
                databaseActions.add(WorkoutDatabaseActionBuilder.create(createWorkoutRequest));

                // Add to clients' scheduled workouts
                // Add to clients' scheduled workout times
                for (String clientID : createWorkoutRequest.clientIDs) {
                    databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledWorkout(clientID, null,
                            true));
                    databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledWorkoutTime(clientID,
                            createWorkoutRequest.time));
                }

                // Add to trainer's scheduled workouts
                // Add to trainer's scheduled workout times
                databaseActions.add(TrainerDatabaseActionBuilder.updateAddScheduledWorkout
                        (createWorkoutRequest.trainerID, null, true));
                databaseActions.add(TrainerDatabaseActionBuilder.updateAddScheduledWorkoutTime
                        (createWorkoutRequest.trainerID, createWorkoutRequest.time));

                // Add to gym's scheduled workouts
                // Add to gym's scheduled workout times
                databaseActions.add(GymDatabaseActionBuilder.updateAddScheduledWorkout
                        (createWorkoutRequest.gymID, null, true));
                databaseActions.add(GymDatabaseActionBuilder.updateAddScheduledWorkoutTime
                        (createWorkoutRequest.gymID, createWorkoutRequest.time));

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
            }
            else {
                throw new Exception("createWorkoutRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createWorkoutRequest not initialized for CREATE statement!");
        }
    }
}
