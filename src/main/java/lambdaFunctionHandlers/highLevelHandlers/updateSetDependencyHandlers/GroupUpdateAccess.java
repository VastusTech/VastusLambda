package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GroupUpdateAccess {
    public static List<DatabaseAction> getActions(String fromID, String groupID, String access) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!(Group.readGroup(groupID).owners.contains(fromID)) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a group that you own!");
        }

        if (!access.equals("private") && !access.equals("public")) {
            throw new Exception("Event access must be \"public\" or \"private\"!");
        }

        // Get all the actions for this process
        databaseActions.add(GroupDatabaseActionBuilder.updateAccess(groupID, access));

        return databaseActions;
    }
}
