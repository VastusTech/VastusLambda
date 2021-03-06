package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a subscription to a Client, allowing the Client to see a Trainer's portal. TODO Review this.
 */
public class ClientAddSubscription {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String subscriptionToID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(clientID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a client you are!");
        }

        // Mutual adding
        databaseActions.add(TrainerDatabaseActionBuilder.updateAddSubscriber(subscriptionToID, clientID));

        databaseActions.add(ClientDatabaseActionBuilder.updateAddSubscription(clientID, subscriptionToID));

        return databaseActions;
    }
}
