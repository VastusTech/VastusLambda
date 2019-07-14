package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;

public class DealUpdateProductCreditPrice {
    public static List<DatabaseAction> getActions(String fromID, String dealID, String productCreditPrice) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!(fromID.equals(Deal.readDeal(dealID).sponsor)) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a deal that you own!");
        }

        // Get all the actions for this process
        // TODO Make the integer parse fail more gracefully
        databaseActions.add(DealDatabaseActionBuilder.updateSetProductCreditPrice(dealID, Integer.parseInt(productCreditPrice)));

        return databaseActions;
    }
}
