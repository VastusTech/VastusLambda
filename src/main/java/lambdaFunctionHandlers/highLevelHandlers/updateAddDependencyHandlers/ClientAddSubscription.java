package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class ClientAddSubscription {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String subscriptionToID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(clientID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a client you are!");
        }

        // Mutual adding
        databaseActions.add(TrainerDatabaseActionBuilder.updateAddSubscriber(subscriptionToID, clientID));

        databaseActions.add(ClientDatabaseActionBuilder.updateAddSubscription(clientID, subscriptionToID));

        return databaseActions;
    }
}
