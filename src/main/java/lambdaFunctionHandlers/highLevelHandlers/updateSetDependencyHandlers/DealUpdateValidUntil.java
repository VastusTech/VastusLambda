package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;

public class DealUpdateValidUntil {
    public static List<DatabaseAction> getActions(String fromID, String dealID, String validUntil) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!(fromID.equals(Deal.readDeal(dealID).sponsor)) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a deal that you own!");
        }

        // TODO Check that the time is also bueno
        new DateTime(validUntil);

        // Get all the actions for this process
        databaseActions.add(DealDatabaseActionBuilder.updateValidUntil(dealID, validUntil));

        return databaseActions;
    }
}
