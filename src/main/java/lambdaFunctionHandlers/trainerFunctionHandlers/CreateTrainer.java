package main.java.lambdaFunctionHandlers.trainerFunctionHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateTrainerRequest;

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
                List<DatabaseAction> databaseActions = new ArrayList<>();
                // Create trainer (with createTrainerRequest)
                databaseActions.add(TrainerDatabaseActionBuilder.create(createTrainerRequest));
                // Add to gym (with gymID and true for fromCreate
                databaseActions.add(GymDatabaseActionBuilder.updateAddTrainerID(createTrainerRequest.gymID,
                        null, true));
                return DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
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
