package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.SponsorDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateSponsorRequest;
import org.joda.time.DateTime;

public class CreateSponsor {
    public static String handle(String fromID, CreateSponsorRequest createSponsorRequest) throws Exception {
        if (createSponsorRequest != null) {
            // Create sponsor
            if (createSponsorRequest.name != null && createSponsorRequest.birthday != null &&
                    createSponsorRequest.email != null && createSponsorRequest.username != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not invalid date or time)
                if (createSponsorRequest.birthday != null) { new DateTime(createSponsorRequest.birthday); }

                databaseActionCompiler.add(SponsorDatabaseActionBuilder.create(createSponsorRequest));

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler);
            }
            else {
                throw new Exception("createSponsorRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createSponsorRequest not initialized for CREATE statement!");
        }
    }
}
