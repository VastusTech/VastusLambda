package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Message;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.databaseActionBuilders.MessageDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateMessageRequest;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static main.java.databaseObjects.Message.getNotificationIDsFromBoard;

public class CreateMessage {
    public static String handle(String fromID, CreateMessageRequest createMessageRequest) throws Exception {
        if (createMessageRequest != null) {
            // Create client
            if (createMessageRequest.from != null && createMessageRequest.board != null && createMessageRequest
                    .message != null && createMessageRequest.name != null) {
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!(fromID.equals(createMessageRequest.from)) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only send messages as yourself!");
                }

                String type = createMessageRequest.type;
                if (type != null) {
                    try {
                         Message.MessageType.valueOf(type);
                    }
                    catch (IllegalArgumentException e) {
                        throw new Exception("Could not understand messageType: " + type);
                    }
                }

                // Check to see if the request features are well formed (i.e not invalid date or time)

                // Add the create statement
                databaseActionCompiler.add(MessageDatabaseActionBuilder.create(createMessageRequest));

                // Send an Ably message!
                JsonObjectBuilder payload = Json.createObjectBuilder()
                    .add("from", createMessageRequest.from)
                    .add("name", createMessageRequest.name)
                    .add("message", createMessageRequest.message)
                    .add("board", createMessageRequest.board);

                if (createMessageRequest.type != null) {
                    payload = payload.add("type", createMessageRequest.type);
                }

                if (createMessageRequest.profileImagePath != null) {
                    payload = payload.add("profileImagePath", createMessageRequest.profileImagePath);
                }

                databaseActionCompiler.addMessage(createMessageRequest.board + "-Board", "Message", payload);

                return DynamoDBHandler.getInstance().attemptTransaction(databaseActionCompiler);
            }
            else {
                throw new Exception("createClientRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createMessageRequest not initialized for CREATE statement!");
        }
    }
}
