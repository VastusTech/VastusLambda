package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateTrainerRequest;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class CreateTrainer {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateTrainerRequest createTrainerRequest, boolean ifWithCreate) throws Exception {
        if (createTrainerRequest != null) {
            // Check required fields
            if (createTrainerRequest.name != null && createTrainerRequest.email != null &&
                    createTrainerRequest.username != null) {
                // Create the database action list for the transaction to complete
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not empty string or invalid date)
                if (createTrainerRequest.birthday != null) { new DateTime(createTrainerRequest.birthday); }
                if (createTrainerRequest.preferredIntensity != null) { Integer.parseInt(createTrainerRequest
                        .preferredIntensity); }
                if (createTrainerRequest.workoutPrice != null) { Integer.parseInt(createTrainerRequest.workoutPrice); }
                if (createTrainerRequest.workoutCapacity != null) { Integer.parseInt(createTrainerRequest
                        .workoutCapacity); }

                // Create trainer (with createTrainerRequest)
                databaseActionCompiler.add(TrainerDatabaseActionBuilder.create(createTrainerRequest, ifWithCreate));

                // Add to gym (with gymID and true for fromCreate
//                databaseActionCompiler.add(GymDatabaseActionBuilder.updateAddTrainer(createTrainerRequest.gym,
//                        null, true));

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("Required fields missing in createTrainerRequest!");
            }
        }
        else {
            throw new Exception("createTrainerRequest not initialized for CREATE statement!");
        }
    }
}
