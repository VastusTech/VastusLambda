package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Message;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.MessageDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateMessageRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 */
public class CreateMessage {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateMessageRequest createMessageRequest, boolean ifWithCreate) throws Exception {
        if (createMessageRequest != null) {
            // Create client
            if (createMessageRequest.from != null && createMessageRequest.board != null && createMessageRequest
                    .message != null && createMessageRequest.name != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
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
                List<String> notificationIDs = Message.getNotificationIDsFromBoard(createMessageRequest.board);

                // If the notifications list is bigger than one, then we know all the IDs are users
                if (notificationIDs.size() > 1) {
                    // Then we add the board to their boards
                    for (String id : notificationIDs) {
                        String itemType = ItemType.getItemType(id);
                        User user = User.readUser(id, itemType);
                        if (!user.messageBoards.contains(createMessageRequest.board)) {
                            databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddMessageBoard(id, itemType, createMessageRequest.board));
                            databaseActionCompiler.getNotificationHandler().addAddNotification(
                                    id,
                                    "messageBoards",
                                    createMessageRequest.board,
                                    false
                            );
                        }
                    }
                }

                // Add the create statement
                databaseActionCompiler.add(MessageDatabaseActionBuilder.create(createMessageRequest, ifWithCreate));

                // Send an Ably message!
                Map<String, Object> payload = new HashMap<>();
                payload.put("from",  createMessageRequest.from);
                payload.put("name", createMessageRequest.name);
                payload.put("message", createMessageRequest.message);
                payload.put("board", createMessageRequest.board);

                if (createMessageRequest.type != null) {
                    payload.put("type", createMessageRequest.type);
                }

                if (createMessageRequest.profileImagePath != null) {
                    payload.put("profileImagePath", createMessageRequest.profileImagePath);
                }

                for (String notifyID : Message.getNotificationIDsFromBoard(createMessageRequest.board)) {
                    if (!notifyID.equals(fromID)) {
                        databaseActionCompiler.getNotificationHandler().setCreateFlag(notifyID);
                    }
                }

                databaseActionCompiler.getNotificationHandler().addMessageNotification(createMessageRequest.board, payload);

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("createMessageRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createMessageRequest not initialized for CREATE statement!");
        }
    }
}
