package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.MessageDatabaseActionBuilder;

/**
 * TODO
 */
public class MessageAddLastSeenFor {
    public static List<DatabaseAction> getActions(String fromID, String board, String messageID, String userID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a read status for yourself!");
        }

        databaseActions.add(MessageDatabaseActionBuilder.updateAddLastSeenFor(board, messageID, userID));

        return databaseActions;
    }
}
