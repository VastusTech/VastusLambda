package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GymUpdatePaymentSplit {
    public static List<DatabaseAction> getActions(String gymID, String paymentSplit) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Check to see if float is well formed
        Double.parseDouble(paymentSplit);

        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateAddress(gymID, paymentSplit));

        return databaseActions;
    }
}
