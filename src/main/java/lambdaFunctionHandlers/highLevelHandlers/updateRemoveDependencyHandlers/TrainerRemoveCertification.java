package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class TrainerRemoveCertification {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String certification) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(trainerID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a trainer you are!");
        }

        // Get all the actions for this process
        databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveCertification(trainerID, certification));

        return databaseActions;
    }
}
