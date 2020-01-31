package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;

public class DealAddScore {
    public static List<DatabaseAction> getActions(String fromID, String dealID, String score) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!(fromID.equals(Deal.readDeal(dealID).sponsor)) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a deal that you own!");
        }

        // TODO CHECK ECH KECH quantity

        // Get all the actions for this process
        databaseActions.add(DealDatabaseActionBuilder.updateAddScore(dealID, Integer.parseInt(score)));

        return databaseActions;
    }
}
