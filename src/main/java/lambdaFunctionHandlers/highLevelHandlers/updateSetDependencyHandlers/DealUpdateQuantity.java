package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.logic.Constants;

public class DealUpdateQuantity {
    public static List<DatabaseAction> getActions(String fromID, String dealID, String quantity) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!(fromID.equals(Deal.readDeal(dealID).sponsor)) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a deal that you own!");
        }

        // TODO Parse int fail gracefully and ensure that it doesn't get below zero

        // Get all the actions for this process
        databaseActions.add(DealDatabaseActionBuilder.updateSetQuantity(dealID, Integer.parseInt(quantity)));

        return databaseActions;
    }
}
