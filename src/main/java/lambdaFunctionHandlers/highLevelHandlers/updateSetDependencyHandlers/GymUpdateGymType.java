package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GymUpdateGymType {
    public static List<DatabaseAction> getActions(String gymID, String gymType) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO Check to see if gymType is well formed

        // Get all the actions for this process
        databaseActions.add(GymDatabaseActionBuilder.updateGymType(gymID, gymType));

        // TODO Set payment split to DEFAULT for whatever it's going into
        databaseActions.add(GymDatabaseActionBuilder.updatePaymentSplit(gymID, Constants.nullAttributeValue));

        return databaseActions;
    }
}
