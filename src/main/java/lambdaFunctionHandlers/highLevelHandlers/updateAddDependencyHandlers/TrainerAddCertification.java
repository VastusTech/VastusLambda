package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a certification String to a Trainer.
 */
public class TrainerAddCertification {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String certification) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(trainerID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a trainer you are!");
        }

        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updateAddCertification(trainerID, certification));

        return databaseActions;
    }
}
