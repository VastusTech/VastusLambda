package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PartyDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePartyRequest;

public class CreateParty {
    public static String handle(CreatePartyRequest createPartyRequest) throws Exception {
        if (createPartyRequest != null) {
            // Create party
            if (createPartyRequest.ownerID != null && createPartyRequest.time != null && createPartyRequest
                    .capacity != null && createPartyRequest.address != null && createPartyRequest.title != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e invalid date)
                new TimeInterval(createPartyRequest.time);
                Integer.parseInt(createPartyRequest.capacity);

                databaseActionCompiler.add(PartyDatabaseActionBuilder.create(createPartyRequest));

                // Add to the owner scheduled parties and scheduled times
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledParty(createPartyRequest
                        .ownerID, null, true));
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime(createPartyRequest.ownerID,
                        createPartyRequest.time));

                // Add to each members scheduled parties and scheduled times
                if (createPartyRequest.memberIDs != null) {
                    for (String memberID : createPartyRequest.memberIDs) {
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledParty(memberID,
                                null, true));
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime(memberID,
                                createPartyRequest.time));
                    }
                }

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
            }
            else {
                throw new Exception("createPartyRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createPartyRequest not initialized for CREATE statement!");
        }
    }
}
