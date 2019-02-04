package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.Logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

public class GroupRemoveTag {
  public static List<DatabaseAction> getActions(String fromID, String groupID, String tag) throws Exception {
    List<DatabaseAction> databaseActions = new ArrayList<>();

    Group group = Group.readGroup(groupID);

    if (!group.owners.contains(fromID) && !fromID.equals(Constants.adminKey)) {
      throw new Exception("PERMISSIONS ERROR: You can only update a group you own!");
    }

    databaseActions.add(GroupDatabaseActionBuilder.updateRemoveTag(groupID, tag));

    return databaseActions;
  }
}
