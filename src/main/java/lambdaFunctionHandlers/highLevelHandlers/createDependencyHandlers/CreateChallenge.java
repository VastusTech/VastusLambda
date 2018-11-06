package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateChallenge {
    public static String handle(CreateChallengeRequest createChallengeRequest) throws Exception {
        if (createChallengeRequest != null) {
            // Create challenge
            if (createChallengeRequest.owner != null && createChallengeRequest.time != null && createChallengeRequest
                    .capacity != null && createChallengeRequest.address != null && createChallengeRequest.title !=
                    null && createChallengeRequest.goal != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not empty string or invalid date)
                new TimeInterval(createChallengeRequest.time);
                Integer.parseInt(createChallengeRequest.capacity);

                if (createChallengeRequest.access != null) {
                    if (!createChallengeRequest.access.equals("public") && !createChallengeRequest.access.equals
                            ("private")) {
                        throw new Exception("Create Challenge access must be either \"public\" or \"private\"!");
                    }
                }

                if (createChallengeRequest.members == null) {
                    createChallengeRequest.members = new String[]{createChallengeRequest.owner};
                }
                else {
                    ArrayList<String> members = new ArrayList<>(Arrays.asList(createChallengeRequest.members));
                    members.add(createChallengeRequest.owner);
                    createChallengeRequest.members = members.toArray(new String[]{});
                }

                databaseActionCompiler.add(ChallengeDatabaseActionBuilder.create(createChallengeRequest));

                // Update owners fields
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddOwnedChallenge(createChallengeRequest
                        .owner, null, true));
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledChallenge
                        (createChallengeRequest.owner, null, true));
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime(createChallengeRequest
                        .owner, createChallengeRequest.time));

                // Update each members fields
                if (createChallengeRequest.members != null) {
                    for (String member : createChallengeRequest.members) {
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledChallenge
                                (member, null, true));
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime
                                (member, createChallengeRequest.time));
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
