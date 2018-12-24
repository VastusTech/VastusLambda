package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers.ChallengeUpdateGroup;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers.EventUpdateGroup;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers.PostUpdateGroup;

import java.util.ArrayList;
import java.util.List;

public class DeleteGroup {
    public static List<DatabaseAction> getActions(String fromID, String groupID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Group group = Group.readGroup(groupID);

        if (!group.owners.contains(fromID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a comment if you authored it!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        // When you delete a Group, all the stuff inside the Group removes their "group" attribute.
        for (String ownerID : group.owners) {
            String ownerItemType = ItemType.getItemType(ownerID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveOwnedGroup(ownerID, ownerItemType, groupID));
        }
        for (String memberID : group.members) {
            String memberItemType = ItemType.getItemType(memberID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveGroup(memberID, memberItemType, groupID));
        }
        for (String eventID : group.events) {
            databaseActions.addAll(EventUpdateGroup.getActions(fromID, eventID, null));
        }
        for (String challengeID : group.challenges) {
            databaseActions.addAll(ChallengeUpdateGroup.getActions(fromID, challengeID, null));
        }
        for (String postID : group.posts) {
            databaseActions.addAll(PostUpdateGroup.getActions(fromID, postID, null));
        }
        for (String inviteID : group.receivedInvites) {
            databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
        }

        // Delete the Group
        databaseActions.add(GroupDatabaseActionBuilder.delete(groupID));

        return databaseActions;
    }
}
