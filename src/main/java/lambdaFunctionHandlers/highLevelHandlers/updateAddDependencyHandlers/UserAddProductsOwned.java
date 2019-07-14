package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseObjects.Product;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.logic.Constants;
import main.java.logic.TimeHelper;

public class UserAddProductsOwned {
    public static List<DatabaseAction> getActions(String fromID, String userID, String userItemType,
                                                  String dealID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only buy a deal as yourself!");
        }

        Deal deal = Deal.readDeal(dealID);

        // TODO Better way to ensure that two Users don't get the same Product!!!!
        // Choose the product for the User to own!
        if (deal.products.size() == 0) {
            throw new Exception("No products remaining!");
        }
        Product product = Product.readProduct(deal.products.stream().skip((int) (deal.products.size() * Math.random())).findFirst().orElse("0"));

        if (deal.validUntil == null || TimeHelper.timeHasPassed(deal.validUntil)) {
            throw new Exception("Time to buy the deal has already passed or has not started yet!");
        }

        databaseActions.addAll(UserExchangeCredit.getActions(fromID, userID, userItemType, deal.sponsor, "Sponsor", deal.productCreditPrice));
        databaseActions.add(DealDatabaseActionBuilder.updateRemoveProduct(dealID, product.id));
        databaseActions.add(UserDatabaseActionBuilder.updateAddProductOwned(userID, userItemType, product.id, false));

        return databaseActions;
    }
}
