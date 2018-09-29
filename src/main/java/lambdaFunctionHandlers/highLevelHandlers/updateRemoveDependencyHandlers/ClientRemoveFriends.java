package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientRemoveFriends {
    public static List<DatabaseAction> getActions(String clientID, String[] friendIDs) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveFriends(clientID, friendIDs));

        for (String friendID : friendIDs) {
            // Mutual Friendship Removal
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveFriends(friendID, new String[]{clientID}));
        }

        return databaseActions;
    }
}
