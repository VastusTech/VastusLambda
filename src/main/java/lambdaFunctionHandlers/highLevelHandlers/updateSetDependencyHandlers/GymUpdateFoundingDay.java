package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;

import java.util.List;

/**
 * TODO
 */
public class GymUpdateFoundingDay {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String foundingDay) throws Exception {
        if (!fromID.equals(gymID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }
        return UserUpdateBirthday.getActions(fromID, gymID, "Gym", foundingDay);
    }
}
