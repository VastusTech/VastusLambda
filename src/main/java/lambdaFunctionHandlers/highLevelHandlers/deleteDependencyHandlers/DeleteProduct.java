package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseObjects.Product;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.ProductDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.logic.Constants;
import main.java.logic.ItemType;

/**
 * Deletes a Product from the database as well as any dependencies on its Product ID.
 */
public class DeleteProduct {
    public static List<DatabaseAction> getActions(String fromID, String productID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Product product = Product.readProduct(productID);
        Deal deal = Deal.readDeal(product.deal);

        if (!fromID.equals(product.owner) && !fromID.equals(deal.sponsor) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a product if you own it or the sponsor owns it!");
        }

        // Remove the product from the owner
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveProductOwned(product.owner,
                ItemType.getItemType(product.owner), productID));

        // Remove the product from the Deal
        databaseActions.add(DealDatabaseActionBuilder.updateRemoveProductSold(product.deal, productID));

        // Delete the Product
        databaseActions.add(ProductDatabaseActionBuilder.delete(productID));

        return databaseActions;
    }
}
