package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateEventRequest;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateEvent {
    public static String handle(CreateEventRequest createEventRequest) throws Exception {
        if (createEventRequest != null) {
            // Create event
            if (createEventRequest.owner != null && createEventRequest.time != null && createEventRequest
                    .capacity != null && createEventRequest.address != null && createEventRequest.title !=
                    null && createEventRequest.ifChallenge != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                // Check to see if the request features are well formed (i.e not empty string or invalid date)
                new TimeInterval(createEventRequest.time);
                Integer.parseInt(createEventRequest.capacity);
                if (Boolean.parseBoolean(createEventRequest.ifChallenge) && createEventRequest.goal == null) {
                    throw new Exception("If the event is a challenge, it needs a goal");
                }

                if (createEventRequest.access != null) {
                    if (!createEventRequest.access.equals("public") && !createEventRequest.access.equals
                            ("private")) {
                        throw new Exception("Create Event access must be either \"public\" or \"private\"!");
                    }
                }

                if (createEventRequest.members == null) {
                    createEventRequest.members = new String[]{createEventRequest.owner};
                }
                else {
                    ArrayList<String> members = new ArrayList<>(Arrays.asList(createEventRequest.members));
                    if (!members.contains(createEventRequest.owner)) {
                        members.add(createEventRequest.owner);
                        createEventRequest.members = members.toArray(new String[]{});
                    }
                }

                databaseActionCompiler.add(EventDatabaseActionBuilder.create(createEventRequest));

                // Update owners fields
                databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddOwnedEvent(createEventRequest
                        .owner, null, true));

                // Update each members fields
                if (createEventRequest.members != null) {
                    for (String member : createEventRequest.members) {
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledEvent
                                (member, null, true));
                        databaseActionCompiler.add(ClientDatabaseActionBuilder.updateAddScheduledTime
                                (member, createEventRequest.time));
                    }
                }

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler.getDatabaseActions());
            }
            else {
                throw new Exception("createEventRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createEventRequest not initialized for CREATE statement!");
        }
    }
}
