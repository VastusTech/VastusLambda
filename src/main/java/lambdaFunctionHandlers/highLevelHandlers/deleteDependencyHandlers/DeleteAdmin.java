package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Admin;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.AdminDatabaseActionBuilder;
import main.java.logic.Constants;

public class DeleteAdmin {
    public static List<DatabaseAction> getActions(String fromID, String adminID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete an admin from console!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        // Delete the user associated with the sponsor
        databaseActions.addAll(DeleteUser.getActions(fromID, Admin.readAdmin(adminID)));

        // Delete the Sponsor
        databaseActions.add(AdminDatabaseActionBuilder.delete(adminID));

        return databaseActions;
    }
}
