package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class TrainerAddAvailableTime {
    public static List<DatabaseAction> getActions(String fromID, String trainerID, String availableTime) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(trainerID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a trainer if it's yourself!");
        }

        // Check the time
        new TimeInterval(availableTime);

        databaseActions.add(TrainerDatabaseActionBuilder.updateAddAvailableTime(trainerID, availableTime));

        return databaseActions;
    }
}
