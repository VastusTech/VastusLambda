package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a tag to a Group, indicating what kind of a Group it is.
 */
public class GroupAddTag {
    public static List<DatabaseAction> getActions(String fromID, String groupID, String tag) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Group group = Group.readGroup(groupID);

        if (fromID == null || (!group.owners.contains(fromID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a gym you own!");
        }

        // TODO Check the tag name?

        databaseActions.add(GroupDatabaseActionBuilder.updateAddTag(groupID, tag));

        return databaseActions;
    }
}
