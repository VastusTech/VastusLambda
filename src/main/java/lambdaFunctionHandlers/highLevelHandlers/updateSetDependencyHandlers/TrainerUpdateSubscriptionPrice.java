package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class TrainerUpdateSubscriptionPrice {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String subscriptionPrice) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(trainerID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a trainer you are!");
        }

        // Check to see if the price is well formed
        Float.parseFloat(subscriptionPrice);

        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updateSubscriptionPrice(trainerID, subscriptionPrice));

        return databaseActions;
    }
}
