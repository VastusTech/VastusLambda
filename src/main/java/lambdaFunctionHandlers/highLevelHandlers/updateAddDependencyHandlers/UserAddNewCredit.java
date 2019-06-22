package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.logic.Constants;

/**
 * Creates new credit and gifts it to a User.
 */
public class UserAddNewCredit {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, int creditAmount) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only receive credit as yourself!");
        }

        // TODO This should only really be called from a Challenge win or other automated event.
        // TODO Come up with an algorithm in which more people in the challenge will increase the
        // TODO amount of credit given?

        databaseActions.add(UserDatabaseActionBuilder.updateAddCredit(userID, itemType, creditAmount));

        return databaseActions;
    }
}
