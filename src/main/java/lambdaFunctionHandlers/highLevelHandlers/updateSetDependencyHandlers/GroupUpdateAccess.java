package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates a Group's access and potentially the access of any Events or Challenges it contains.
 */
public class GroupUpdateAccess {
    public static List<DatabaseAction> getActions(String fromID, String groupID, String access) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!(Group.readGroup(groupID).owners.contains(fromID)) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update a group that you own!");
        }

        if (!access.equals("private") && !access.equals("public")) {
            throw new Exception("Event access must be \"public\" or \"private\"!");
        }

        Group group = Group.readGroup(groupID);
        if (access.equals("public")) {
            for (String ownerID : group.owners) {
                if (ItemType.getItemType(ownerID).equals("Client")) {
                    throw new Exception("A Client cannot be an owner in a public Group!");
                }
            }
        }

        // Get all the actions for this process
        databaseActions.add(GroupDatabaseActionBuilder.updateAccess(groupID, access));

        // Update all Group's challenges
        for (String challengeID : group.challenges) {
            databaseActions.addAll(ChallengeUpdateAccess.getActions(fromID, challengeID, access));
        }

        // Update all Group's Events
        for (String eventID : group.events) {
            databaseActions.addAll(EventUpdateAccess.getActions(fromID, eventID, access));
        }

        return databaseActions;
    }
}
