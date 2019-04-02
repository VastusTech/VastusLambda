package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseObjects.Enterprise;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a Client in the database, checks the inputs, and checks the enterprise ID to make
 * sure that it is real.
 */
public class CreateClient {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateClientRequest createClientRequest, boolean ifWithCreate) throws Exception {
        if (createClientRequest != null) {
            // Create client
            if (createClientRequest.name != null && createClientRequest.email != null && createClientRequest.username != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not invalid date or time)
                if (createClientRequest.birthday != null) { new DateTime(createClientRequest.birthday); }

                // TODO Check the enterprise ID
                // TODO This will need to be changed to be more secure in the future!!!
                if (createClientRequest.enterpriseID != null) {
                    Enterprise.readEnterprise(createClientRequest.enterpriseID);
                }

                databaseActionCompiler.add(ClientDatabaseActionBuilder.create(createClientRequest, ifWithCreate));

                compilers.add(databaseActionCompiler);

                return compilers;
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
