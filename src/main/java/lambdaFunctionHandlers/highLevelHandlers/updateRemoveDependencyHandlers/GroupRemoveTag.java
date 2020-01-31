package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

/**
 * Removes a tag from a Group.
 */
public class GroupRemoveTag {
  public static List<DatabaseAction> getActions(String fromID, String groupID, String tag) throws Exception {
    List<DatabaseAction> databaseActions = new ArrayList<>();

    Group group = Group.readGroup(groupID);

    if (fromID == null || (!group.owners.contains(fromID) && !Constants.isAdmin(fromID))) {
      throw new PermissionsException("You can only update a group you own!");
    }

    databaseActions.add(GroupDatabaseActionBuilder.updateRemoveTag(groupID, tag));

    return databaseActions;
  }
}
