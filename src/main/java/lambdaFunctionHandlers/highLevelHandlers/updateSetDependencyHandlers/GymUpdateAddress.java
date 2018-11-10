package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseObjects.Gym;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GymUpdateAddress {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String address) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(gymID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }

        // TODO Check to see if the address is valid?

        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateAddress(gymID, address));

        return databaseActions;
    }
}
