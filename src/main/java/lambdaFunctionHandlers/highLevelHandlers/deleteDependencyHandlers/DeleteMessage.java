package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Message;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.MessageDatabaseActionBuilder;
import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;

/**
 * Deletes a Message from the database without deleting any other dependencies.
 */
public class DeleteMessage {
    public static List<DatabaseAction> getActions(String fromID, String board, String messageID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Message message = Message.readMessage(board, messageID);

        if (fromID == null || (!message.from.equals(fromID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only delete a message if you made it!");
        }

        // Delete the Message
        databaseActions.add(MessageDatabaseActionBuilder.delete(board, messageID));

        return databaseActions;
    }
}
