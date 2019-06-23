package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.logic.Constants;

public class DealUpdateProductStoreLink {
    public static List<DatabaseAction> getActions(String fromID, String dealID, String productStoreLink) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!(fromID.equals(Deal.readDeal(dealID).sponsor)) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a deal that you own!");
        }

        // Get all the actions for this process
        databaseActions.add(DealDatabaseActionBuilder.updateProductStoreLink(dealID, productStoreLink));

        return databaseActions;
    }
}
