package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientRemoveSubscription {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String subscriptionToID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(clientID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a client you are!");
        }

        // Mutual removing
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveSubscriber(subscriptionToID, clientID));

        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveSubscription(clientID, subscriptionToID));

        return databaseActions;
    }
}
