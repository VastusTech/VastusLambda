package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.logic.Constants;

/**
 * Buys a Deal from a Sponsor to a User and handles all credit payments involved.
 */
public class UserBuyDeal {
    public static List<DatabaseAction> getActions(String fromID, String userID, String userItemType,
                                                  String dealID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only buy a deal as yourself!");
        }

        Deal deal = Deal.readDeal(dealID);

        if (deal.validTime != null && !deal.validTime.nowIsWithin()) {
            throw new Exception("Time to buy the deal has already passed or has not started yet!");
        }

        databaseActions.addAll(UserExchangeCredit.getActions(fromID, userID, userItemType, deal.sponsor, "Sponsor", deal.productCreditPrice));
        databaseActions.add(DealDatabaseActionBuilder.updateRemoveQuantity(dealID, 1));

        // TODO Also handle any ordering of materials here? Or maybe on the client?

        return databaseActions;
    }
}
