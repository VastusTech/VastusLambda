package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientRemoveFriendRequest {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String friendID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(clientID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only decline friend requests for yourself!");
        }

        // Remove the friend request from yourself!
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveFriendRequest(clientID, friendID));

        return databaseActions;
    }
}
