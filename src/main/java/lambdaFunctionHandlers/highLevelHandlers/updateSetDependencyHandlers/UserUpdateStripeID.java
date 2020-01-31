package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

/**
 * Update a User's Stripe ID.
 */
public class UserUpdateStripeID {
  public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String stripeID) throws Exception {
    List<DatabaseAction> databaseActions = new ArrayList<>();

    if (fromID == null || (!fromID.equals(userID) && !Constants.isAdmin(fromID))) {
      throw new PermissionsException("You can only update a user you are!");
    }
    // Get all the actions for this process
    databaseActions.add(UserDatabaseActionBuilder.updateStripeID(userID, itemType, stripeID));

    return databaseActions;
  }
}
