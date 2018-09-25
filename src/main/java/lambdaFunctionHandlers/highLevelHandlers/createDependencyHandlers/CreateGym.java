package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGymRequest;

import java.util.ArrayList;
import java.util.List;

public class CreateGym {
    public static String handle(CreateGymRequest createGymRequest) throws Exception {
        if (createGymRequest != null) {
            if (createGymRequest.name != null && createGymRequest.foundingDay != null && createGymRequest.email !=
                    null && createGymRequest.username != null && createGymRequest.address != null && createGymRequest
                    .sessionCapacity != null) {

                List<DatabaseAction> databaseActions = new ArrayList<>();

                // TODO Check to see if the request features are well formed (i.e not empty string or invalid date)

                // Create Gym
                databaseActions.add(GymDatabaseActionBuilder.create(createGymRequest));

                // Do the transaction and return the ID afterwards
                return DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
            }
            else {
                throw new Exception("createGymRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createGymRequest not initialized for CREATE statement!");
        }
    }
}
