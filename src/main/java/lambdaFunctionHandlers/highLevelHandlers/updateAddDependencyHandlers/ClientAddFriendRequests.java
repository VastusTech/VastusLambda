package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientAddFriendRequests {
    public static List<DatabaseAction> getActions(String clientID, String[] friendIDs) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        databaseActions.add(ClientDatabaseActionBuilder.updateAddFriendRequests(clientID, friendIDs));

        return databaseActions;
    }
}
