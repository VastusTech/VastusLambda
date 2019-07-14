package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Sponsor;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.SponsorDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes a Sponsor from the database as well as any dependencies there may be to its Sponsor ID.
 */
public class DeleteSponsor {
    public static List<DatabaseAction> getActions(String fromID, String sponsorID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(sponsorID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only delete a client if it's yourself!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        Sponsor sponsor = Sponsor.readSponsor(sponsorID);

        // Delete the user associated with the sponsor
        databaseActions.addAll(DeleteUser.getActions(fromID, sponsor));

        // Delete the Sponsor
        databaseActions.add(SponsorDatabaseActionBuilder.delete(sponsorID));

        return databaseActions;
    }
}
