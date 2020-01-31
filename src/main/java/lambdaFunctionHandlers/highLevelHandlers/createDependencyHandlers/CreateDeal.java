package main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Deal;
import main.java.databaseObjects.Sponsor;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.databaseActionBuilders.DealDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.SponsorDatabaseActionBuilder;
import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.lambdaFunctionHandlers.requestObjects.CreateDealRequest;
import main.java.logic.Constants;

/**
 * Creates a {@link Deal} in the database, checks the inputs, and adds it to the author's comments and
 * the Post/Comment/Submission's comments.
 */
public class CreateDeal {
    public static List<DatabaseActionCompiler> getCompilers(String fromID, CreateDealRequest createDealRequest, int depth) throws Exception {
        if (createDealRequest != null) {
            // Create client
            if (createDealRequest.sponsor != null && createDealRequest.productName != null &&
                    createDealRequest.productCreditPrice != null && createDealRequest.productType != null) {
                List<DatabaseActionCompiler> compilers = new ArrayList<>();
                DatabaseActionCompiler databaseActionCompiler = new DatabaseActionCompiler();

                if (fromID == null || (!fromID.equals(createDealRequest.sponsor) && !Constants.isAdmin(fromID))) {
                    throw new PermissionsException("You can only create deals you will own!");
                }

                // TODO Check that the quantity is not less than zero and also the other integer things now that I think about it lol
                // TODO quantity can also be null
                Sponsor.readSponsor(createDealRequest.sponsor);

                // Create the object
                if (depth == 0) {
                    databaseActionCompiler.add(DealDatabaseActionBuilder.create(createDealRequest, null));
                }
                else {
                    // TODO If we ever try to create Deals automatically, figure out which
                    // TODO attributes need which passover Identifiers.
                    databaseActionCompiler.add(DealDatabaseActionBuilder.create(createDealRequest, null));
                }

                databaseActionCompiler.add(SponsorDatabaseActionBuilder.updateAddDeal(createDealRequest.sponsor, null, true));

                compilers.add(databaseActionCompiler);

                return compilers;
            }
            else {
                throw new Exception("createDealRequest is missing required fields!");
            }
        }
        else {
            throw new Exception("createDealRequest not initialized for CREATE statement!");
        }
    }
}
