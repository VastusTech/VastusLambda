package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.ProductDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers.UserExchangeCredit;
import main.java.lambdaFunctionHandlers.requestObjects.CreateProductRequest;
import main.java.logic.Constants;
import main.java.logic.ItemType;

public class CreateProduct {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateProductRequest createProductRequest, int depth) throws Exception {
        if (createProductRequest != null) {
            // Create client
            if (createProductRequest.deal != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (!fromID.equals(createProductRequest.owner) && !fromID.equals(Constants.adminKey)) {
                    throw new Exception("PERMISSIONS ERROR: You can only buy Products for yourself!");
                }

                // TODO Check the attributes
                Deal deal = Deal.readDeal(createProductRequest.deal);

                // Create the object
                if (depth == 0) {
                    databaseActionCompiler.add(ProductDatabaseActionBuilder.create(createProductRequest, null));
                }
                else {
                    // TODO If we ever try to create Products automatically, figure out which
                    // TODO attributes need which passover Identifiers.
                    databaseActionCompiler.add(ProductDatabaseActionBuilder.create(createProductRequest, null));
                }

                if (createProductRequest.owner != null) {
                    // Then the Product is already owned
                    String itemType = ItemType.getItemType(createProductRequest.owner);
                    databaseActionCompiler.add(UserDatabaseActionBuilder.updateAddProductOwned(
                            createProductRequest.owner,
                            itemType,
                            null, true
                    ));
                    databaseActionCompiler.add(DealDatabaseActionBuilder.updateAddProductSold(createProductRequest.deal, null, true));
                    databaseActionCompiler.addAll(UserExchangeCredit.getActions(fromID, createProductRequest.owner,
                            ItemType.getItemType(createProductRequest.owner), deal.sponsor, "Sponsor", deal.productCreditPrice));
                }
                else {
                    // Otherwise it is still owned by the deal
                    databaseActionCompiler.add(DealDatabaseActionBuilder.updateAddProduct(createProductRequest.deal, null, true));
                }

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("createProductRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createProductRequest not initialized for CREATE statement!");
        }
    }
}
