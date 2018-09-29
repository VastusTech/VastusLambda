package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;

public class CreateChallenge {
    public static String handle(CreateChallengeRequest createChallengeRequest) throws Exception {
        if (createChallengeRequest != null) {
            // Create challenge
            if (createChallengeRequest.ownerID != null && createChallengeRequest.time != null && createChallengeRequest
                    .capacity != null && createChallengeRequest.address != null && createChallengeRequest.title !=
                    null && createChallengeRequest.goal != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // TODO Check to see if the request features are well formed (i.e not empty string or invalid date)
                databaseActionCompiler.add(ChallengeDatabaseActionBuilder.create(createChallengeRequest));

                // Update owners fields
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledChallenge
                        (createChallengeRequest.ownerID, null, true));
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime(createChallengeRequest
                        .ownerID, createChallengeRequest.time));

                // Update each members fields
                if (createChallengeRequest.memberIDs != null) {
                    for (String memberID : createChallengeRequest.memberIDs) {
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledChallenge
                                (createChallengeRequest.ownerID, null, true));
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime(createChallengeRequest
                                .ownerID, createChallengeRequest.time));
                    }
                }

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
            }
            else {
                throw new Exception("createChallengeRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createChallengeRequest not initialized for CREATE statement!");
        }
    }
}