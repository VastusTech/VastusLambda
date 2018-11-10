package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class CreateClient {
    public static String handle(String fromID, CreateClientRequest createClientRequest) throws Exception {
        if (createClientRequest != null) {
            // Create client
            if (createClientRequest.name != null && createClientRequest.gender != null && createClientRequest
                    .birthday != null && createClientRequest.email != null && createClientRequest.username != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not invalid date or time)
                new DateTime(createClientRequest.birthday);

                databaseActionCompiler.add(ClientDatabaseActionBuilder.create(createClientRequest));

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
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
