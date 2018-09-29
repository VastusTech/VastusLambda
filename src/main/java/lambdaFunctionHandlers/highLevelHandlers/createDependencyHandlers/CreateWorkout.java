package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DatabaseActionCompiler;
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

                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e invalid date)
                new TimeInterval(createWorkoutRequest.time);
                Integer.parseInt(createWorkoutRequest.capacity);
                Integer.parseInt(createWorkoutRequest.intensity);
                Float.parseFloat(createWorkoutRequest.price);

                // Create Workout
                databaseActionCompiler.add(WorkoutDatabaseActionBuilder.create(createWorkoutRequest));

                // Add to clients' scheduled workouts
                // Add to clients' scheduled workout times
                for (String clientID : createWorkoutRequest.clientIDs) {
                    databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledWorkout(clientID, null,
                            true));
                    databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime(clientID,
                            createWorkoutRequest.time));
                }

                // Add to trainer's scheduled workouts
                // Add to trainer's scheduled workout times
                databaseActionCompiler.add(TrainerDatabaseActionBuilder.updateAddScheduledWorkout
                        (createWorkoutRequest.trainerID, null, true));
                databaseActionCompiler.add(TrainerDatabaseActionBuilder.updateAddScheduledTime
                        (createWorkoutRequest.trainerID, createWorkoutRequest.time));

                // Add to gym's scheduled workouts
                // Add to gym's scheduled workout times
                databaseActionCompiler.add(GymDatabaseActionBuilder.updateAddScheduledWorkout
                        (createWorkoutRequest.gymID, null, true));
                databaseActionCompiler.add(GymDatabaseActionBuilder.updateAddScheduledTime
                        (createWorkoutRequest.gymID, createWorkoutRequest.time));

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
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
