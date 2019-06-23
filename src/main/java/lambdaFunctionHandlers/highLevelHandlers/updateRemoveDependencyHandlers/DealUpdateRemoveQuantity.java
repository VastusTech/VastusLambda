package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.logic.Constants;

public class DealUpdateRemoveQuantity {
    public static List<DatabaseAction> getActions(String fromID, String dealID, String quantity) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!(fromID.equals(Deal.readDeal(dealID).sponsor)) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a deal that you own!");
        }

        // TODO CHECK ECH KECH quantity

        // Get all the actions for this process
        databaseActions.add(DealDatabaseActionBuilder.updateRemoveQuantity(dealID, Integer.parseInt(quantity)));

        return databaseActions;
    }
}
