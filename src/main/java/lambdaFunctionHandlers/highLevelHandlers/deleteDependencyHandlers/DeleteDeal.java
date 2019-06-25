package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.SponsorDatabaseActionBuilder;
import main.java.logic.Constants;

/**
 * Deletes a Deal from the database as well as any dependencies on its Deal ID.
 */
public class DeleteDeal {
    public static List<DatabaseAction> getActions(String fromID, String dealID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Deal deal = Deal.readDeal(dealID);

        if (!fromID.equals(deal.sponsor) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a deal if you own it!");
        }

        // Delete all the products sold once you completely delete the Deal.
        for (String productID : deal.productsSold) {
            databaseActions.addAll(DeleteProduct.getActions(fromID, productID));
        }

        // Remove the deal from the Sponsor
        databaseActions.add(SponsorDatabaseActionBuilder.updateRemoveDeal(deal.sponsor, dealID));

        // Delete the Deal
        databaseActions.add(DealDatabaseActionBuilder.delete(dealID));

        return databaseActions;
    }
}
