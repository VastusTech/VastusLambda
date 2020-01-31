package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Group's title.
 */
public class GroupUpdateTitle {
    public static List<DatabaseAction> getActions(String fromID, String groupID, String title) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!(Group.readGroup(groupID).owners.contains(fromID)) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a group that you own!");
        }

        // Get all the actions for this process
        databaseActions.add(GroupDatabaseActionBuilder.updateTitle(groupID, title));

        return databaseActions;
    }
}
