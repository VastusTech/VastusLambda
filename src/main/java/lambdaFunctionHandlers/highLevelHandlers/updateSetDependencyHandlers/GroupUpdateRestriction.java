package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Group's restriction level, which forces Users to request joining the Group.
 */
public class GroupUpdateRestriction {
    static public List<DatabaseAction> getActions(String fromID, String groupID, String restriction) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Group group = Group.readGroup(groupID);
        if (!(group.owners.contains(fromID)) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a group that you own!");
        }

        if (restriction != null && !restriction.equals("invite")) {
            throw new Exception("restriction value must be either nothing or \"invite\"");
        }

        databaseActions.add(GroupDatabaseActionBuilder.updateRestriction(groupID, restriction));

        return databaseActions;
    }
}
