package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Gym's address.
 */
public class GymUpdateAddress {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String address) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(gymID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a gym you own!");
        }

        // TODO Check to see if the address is valid?

        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateAddress(gymID, address));

        return databaseActions;
    }
}
