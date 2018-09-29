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
    public static String handle(CreateTrainerRequest createTrainerRequest) throws Exception {
        if (createTrainerRequest != null) {
            // Check required fields
            if (createTrainerRequest.name != null && createTrainerRequest.gender != null && createTrainerRequest
                    .birthday != null && createTrainerRequest.email != null && createTrainerRequest.username != null
                    && createTrainerRequest.gymID != null && createTrainerRequest.workoutSticker != null &&
                    createTrainerRequest.preferredIntensity != null) {
                // Create the database action list for the transaction to complete
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not empty string or invalid date)
                new DateTime(createTrainerRequest.birthday);
                Integer.parseInt(createTrainerRequest.preferredIntensity);
                if (createTrainerRequest.workoutPrice != null) { Integer.parseInt(createTrainerRequest.workoutPrice); }
                if (createTrainerRequest.workoutCapacity != null) { Integer.parseInt(createTrainerRequest
                        .workoutCapacity); }

                // Create trainer (with createTrainerRequest)
                databaseActionCompiler.add(TrainerDatabaseActionBuilder.create(createTrainerRequest));

                // Add to gym (with gymID and true for fromCreate
                databaseActionCompiler.add(GymDatabaseActionBuilder.updateAddTrainerID(createTrainerRequest.gymID,
                        null, true));

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
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
