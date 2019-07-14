package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Group's description.
 */
public class GroupUpdateDescription {
    public static List<DatabaseAction> getActions(String fromID, String groupID, String description) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!(Group.readGroup(groupID).owners.contains(fromID)) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a group that you own!");
        }

        // Get all the actions for this process
        databaseActions.add(GroupDatabaseActionBuilder.updateDescription(groupID, description));

        return databaseActions;
    }
}
