package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientAddFriends {
    public static List<DatabaseAction> getActions(String clientID, String[] friendIDs) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        databaseActions.add(ClientDatabaseActionBuilder.updateAddFriends(clientID, friendIDs, true));
        // Get all the actions for this process
        for (String friendID : friendIDs) {
            // Mutual friendships
            databaseActions.add(ClientDatabaseActionBuilder.updateAddFriends(friendID, new String[]{clientID}, false));
        }

        return databaseActions;
    }
}
