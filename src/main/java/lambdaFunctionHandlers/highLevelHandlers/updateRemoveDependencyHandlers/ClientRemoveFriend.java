package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientRemoveFriend {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String friendID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(clientID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only unfriend people for yourself!");
        }

        // Get all the actions for this process
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveFriend(clientID, friendID));

        // Mutual Friendship Removal
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveFriend(friendID, clientID));

        return databaseActions;
    }
}
