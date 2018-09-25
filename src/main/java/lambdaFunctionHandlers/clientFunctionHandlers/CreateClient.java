package main.java.lambdaFunctionHandlers.clientFunctionHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;

import java.util.ArrayList;
import java.util.List;

public class CreateClient {
    public static String handle(CreateClientRequest createClientRequest) throws Exception {
        if (createClientRequest != null) {
            // Create client
            if (createClientRequest.name != null && createClientRequest.gender != null && createClientRequest
                    .birthday != null && createClientRequest.email != null && createClientRequest.username != null) {
                List<DatabaseAction> databaseActions = new ArrayList<>();
                databaseActions.add(ClientDatabaseActionBuilder.create(createClientRequest));
                return DynamoDBHandler.getInstance().attemptTransaction(databaseActions);
            }
            else {
                throw new Exception("createClientRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createClientRequest not initialized for CREATE statement!");
        }
    }
}
