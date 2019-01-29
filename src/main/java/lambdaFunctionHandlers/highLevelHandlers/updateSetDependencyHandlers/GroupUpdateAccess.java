package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.exceptions.ItemNotFoundException;

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
