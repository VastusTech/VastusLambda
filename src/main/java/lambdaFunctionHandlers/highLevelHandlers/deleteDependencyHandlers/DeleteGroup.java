package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers.PostUpdateGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes a Group from the database as well as removes any dependencies on its Group ID.
 */
public class DeleteGroup {
    public static List<DatabaseAction> getActions(String fromID, String groupID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Group group = Group.readGroup(groupID);

        if (fromID == null || (!group.owners.contains(fromID) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only delete a comment if you authored it!");
        }

        // Remove from the owners
        for (String ownerID : group.owners) {
            String ownerItemType = ItemType.getItemType(ownerID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveOwnedGroup(ownerID, ownerItemType, groupID));
        }

        // Remove from the group members
        for (String memberID : group.members) {
            User user = User.readUser(memberID);
            String memberItemType = ItemType.getItemType(memberID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveGroup(memberID, memberItemType, groupID));

            // Also check their sentInvites and check to see if they sent any invites for this group
            for (String inviteID : user.sentInvites) {
                Invite invite = Invite.readInvite(inviteID);
                if (invite.about.equals(groupID)) {
                    databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
                }
            }
        }

        // Remove the received invites for the Group
        for (String inviteID : group.receivedInvites) {
            databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
        }

        // Delete the Events inside the Group
        for (String eventID : group.events) {
            // TODO Instead of deleting maybe we can just complete them?
            databaseActions.addAll(DeleteEvent.getActions(fromID, eventID));
        }

        // Delete the completed events inside the Group
        for (String eventID : group.completedEvents) {
            // TODO Instead of deleting maybe we can just complete them?
            databaseActions.addAll(DeleteEvent.getActions(fromID, eventID));
        }

        // Delete the Challenges inside the Group
        for (String challengeID : group.challenges) {
            // TODO Instead of deleting maybe we can just complete them?
            databaseActions.addAll(DeleteChallenge.getActions(fromID, challengeID));
        }

        // Delete the completed Challenge inside the Group
        for (String challengeID : group.completedChallenges) {
            // TODO Instead of deleting maybe we can just complete them?
            databaseActions.addAll(DeleteChallenge.getActions(fromID, challengeID));
        }

        // Delete all posts for this Group
        for (String postID : group.posts) {
            databaseActions.addAll(DeletePost.getActions(fromID, postID));
        }

        // Delete all streaks associated with this group
        for (String streakID : group.streaks) {
            databaseActions.addAll(DeleteStreak.getActions(fromID, streakID));
        }

        // Delete the Group
        databaseActions.add(GroupDatabaseActionBuilder.delete(groupID));

        return databaseActions;
    }
}
