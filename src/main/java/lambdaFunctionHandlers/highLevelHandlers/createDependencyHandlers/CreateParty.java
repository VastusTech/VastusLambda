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
            if (createPartyRequest.owner != null && createPartyRequest.time != null && createPartyRequest
                    .capacity != null && createPartyRequest.address != null && createPartyRequest.title != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e invalid date)
                new TimeInterval(createPartyRequest.time);
                Integer.parseInt(createPartyRequest.capacity);

                databaseActionCompiler.add(PartyDatabaseActionBuilder.create(createPartyRequest));

                // Add to the owner owned parties scheduled parties and scheduled times
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddOwnedParty(createPartyRequest.owner,
                        null, true));
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledParty(createPartyRequest
                        .owner, null, true));
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime(createPartyRequest.owner,
                        createPartyRequest.time));

                // Add to each members scheduled parties and scheduled times
                if (createPartyRequest.members != null) {
                    for (String member : createPartyRequest.members) {
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledParty(member,
                                null, true));
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime(member,
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
