package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the amount of payment split that the Gym allocates for itself vs. its Trainers. TODO revisit
 */
public class GymUpdatePaymentSplit {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String paymentSplit) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(gymID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }
        // Check to see if float is well formed
        Double.parseDouble(paymentSplit);

        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateAddress(gymID, paymentSplit));

        return databaseActions;
    }
}
