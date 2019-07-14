package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;

import java.util.List;

/**
 * Updates a gym's founding day (which is pretty much its birthday).
 */
public class GymUpdateFoundingDay {
    public static List<DatabaseAction> getActions(String fromID, String gymID, String foundingDay) throws Exception {
        if (fromID == null || (!fromID.equals(gymID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a gym you own!");
        }
        return UserUpdateBirthday.getActions(fromID, gymID, "Gym", foundingDay);
    }
}
