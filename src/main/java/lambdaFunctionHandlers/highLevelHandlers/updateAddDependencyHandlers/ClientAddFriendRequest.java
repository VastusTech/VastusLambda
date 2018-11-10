package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientAddFriendRequest {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String friendID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(friendID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only send friend requests for yourself!");
        }

        // Add the friend request to the person you want to befriend
        databaseActions.add(ClientDatabaseActionBuilder.updateAddFriendRequest(clientID, friendID));

        return databaseActions;
    }
}
