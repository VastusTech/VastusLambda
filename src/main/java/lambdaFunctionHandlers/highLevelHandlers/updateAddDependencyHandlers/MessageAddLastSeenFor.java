package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.MessageDatabaseActionBuilder;

/**
 * Adds a user to a Message's last seen for, indicating that at one point, this was the last message
 * that the User has seen. Used for figuring out unread messages.
 */
public class MessageAddLastSeenFor {
    public static List<DatabaseAction> getActions(String fromID, String board, String messageID, String userID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(userID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a read status for yourself!");
        }

        databaseActions.add(MessageDatabaseActionBuilder.updateAddLastSeenFor(board, messageID, userID));

        return databaseActions;
    }
}
