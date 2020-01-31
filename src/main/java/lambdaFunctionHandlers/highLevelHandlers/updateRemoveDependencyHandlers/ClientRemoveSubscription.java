package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Removes a Trainer Portal Subscription from a Client and from the Trainer's subscribers.
 */
public class ClientRemoveSubscription {
    public static List<DatabaseAction> getActions(String fromID, String clientID, String subscriptionToID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(clientID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a client you are!");
        }

        // Mutual removing
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveSubscriber(subscriptionToID, clientID));

        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveSubscription(clientID, subscriptionToID));

        return databaseActions;
    }
}
