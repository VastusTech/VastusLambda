package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Update a gym's gym type (which indicates payment for the Trainer's information). TODO revisit
 */
public class GymUpdateGymType {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String gymType) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(gymID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a gym you own!");
        }
        // TODO Check to see if gymType is well formed

        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateGymType(gymID, gymType));

        // TODO Set payment split to DEFAULT for whatever it's going into
        databaseActions.add(GymDatabaseActionBuilder.updatePaymentSplit(gymID, "50"));

        return databaseActions;
    }
}
