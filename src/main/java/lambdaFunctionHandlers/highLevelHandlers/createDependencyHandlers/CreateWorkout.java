package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateWorkoutRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a Workout in the database, checks the inputs, and adds the workout to the Trainer and the
 * Client members.
 */
public class CreateWorkout {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateWorkoutRequest createWorkoutRequest, boolean ifWithCreate) throws Exception {
        if (createWorkoutRequest != null) {
            if (createWorkoutRequest.time != null && createWorkoutRequest.trainer != null && createWorkoutRequest
                    .clients != null && createWorkoutRequest.gym != null && createWorkoutRequest.capacity != null
                    && createWorkoutRequest.sticker != null && createWorkoutRequest.intensity != null &&
                    createWorkoutRequest.price != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!Arrays.asList(createWorkoutRequest.clients).contains(fromID) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only create a workout you'll be a part of!");
                }

                // Check to see if the request features are well formed (i.e invalid date)
                new TimeInterval(createWorkoutRequest.time);
                Integer.parseInt(createWorkoutRequest.capacity);
                Integer.parseInt(createWorkoutRequest.intensity);
                Float.parseFloat(createWorkoutRequest.price);

                // Create Workout
                databaseActionCompiler.add(WorkoutDatabaseActionBuilder.create(createWorkoutRequest, ifWithCreate));

                // Add to clients' scheduled workouts
                // Add to clients' scheduled workout times
                for (String client : createWorkoutRequest.clients) {
                    databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddScheduledWorkout(client, "Client",
                            null, true));
                    databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddScheduledTime(client, "Client",
                            createWorkoutRequest.time, null));
                }

                // Add to trainer's scheduled workouts
                // Add to trainer's scheduled workout times
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddScheduledWorkout
                        (createWorkoutRequest.trainer, "Trainer", null, true));
                databaseActionCompiler.add(TrainerDatabaseActionBuilder.updateAddScheduledTime
                        (createWorkoutRequest.trainer, createWorkoutRequest.time, true));

                // Add to gym's scheduled workouts
                // Add to gym's scheduled workout times
                databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddScheduledWorkout
                        (createWorkoutRequest.gym, "Gym", null, true));
                databaseActionCompiler.add(GymDatabaseActionBuilder.updateAddScheduledTime
                        (createWorkoutRequest.gym, createWorkoutRequest.time, true));

                compilers.add(databaseActionCompiler);

                return compilers;
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
