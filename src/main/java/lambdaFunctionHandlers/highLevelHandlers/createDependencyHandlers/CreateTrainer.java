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
    public static String handle(String fromID, CreateTrainerRequest createTrainerRequest) throws Exception {
        if (createTrainerRequest != null) {
            // Check required fields
            if (createTrainerRequest.name != null && createTrainerRequest.email != null &&
                    createTrainerRequest.username != null) {
                // Create the database action list for the transaction to complete
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not empty string or invalid date)
                if (createTrainerRequest.birthday != null) { new DateTime(createTrainerRequest.birthday); }
                if (createTrainerRequest.preferredIntensity != null) { Integer.parseInt(createTrainerRequest
                        .preferredIntensity); }
                if (createTrainerRequest.workoutPrice != null) { Integer.parseInt(createTrainerRequest.workoutPrice); }
                if (createTrainerRequest.workoutCapacity != null) { Integer.parseInt(createTrainerRequest
                        .workoutCapacity); }

                // Create trainer (with createTrainerRequest)
                databaseActionCompiler.add(TrainerDatabaseActionBuilder.create(createTrainerRequest));

                // Add to gym (with gymID and true for fromCreate
//                databaseActionCompiler.add(GymDatabaseActionBuilder.updateAddTrainer(createTrainerRequest.gym,
//                        null, true));

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler);
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
