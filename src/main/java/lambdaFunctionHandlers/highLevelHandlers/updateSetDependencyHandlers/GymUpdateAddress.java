package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GymUpdateAddress {
    public static List<DatabaseAction> getActions(String gymID, String address) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO Check to see if the address is valid?

        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateAddress(gymID, address));

        return databaseActions;
    }
}
