package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientAddFriend {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String friendID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(clientID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only accept friend requests for yourself!");
        }

        // Add the friend to your own friends list
        databaseActions.add(ClientDatabaseActionBuilder.updateAddFriend(clientID, friendID, true));

        // Mutual friendships
        databaseActions.add(ClientDatabaseActionBuilder.updateAddFriend(friendID, clientID, false));

        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveFriendRequest(clientID, friendID));

        return databaseActions;
    }
}
