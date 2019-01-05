package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class GroupAddTag {
    public static List<DatabaseAction> getActions(String fromID, String groupID, String tag) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Group group = Group.readGroup(groupID);

        if (!group.owners.contains(fromID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }

        // TODO Check the tag name?

        databaseActions.add(GroupDatabaseActionBuilder.updateAddTag(groupID, tag));

        return databaseActions;
    }
}