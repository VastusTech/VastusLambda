package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.SponsorDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateSponsorRequest;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a Sponsor in the database and checks the inputs.
 */
public class CreateSponsor {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateSponsorRequest createSponsorRequest, int depth) throws Exception {
        if (createSponsorRequest != null) {
            // Create sponsor
            if (createSponsorRequest.name != null && createSponsorRequest.email != null
                    && createSponsorRequest.username != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not invalid date or time)
                if (createSponsorRequest.birthday != null) { new DateTime(createSponsorRequest.birthday); }

                // TODO If we ever try to create Sponsors automatically, figure out which
                // TODO attributes need which passover Identifiers.
                databaseActionCompiler.add(SponsorDatabaseActionBuilder.create(createSponsorRequest, null));

                compilers.add(databaseActionCompiler);

                return compilers;
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
