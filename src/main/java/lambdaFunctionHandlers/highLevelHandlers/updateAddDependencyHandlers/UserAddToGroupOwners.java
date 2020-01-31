package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteInvite;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a User to the Group Owners, allowing them to have full administrative access to the Group,
 * checks the requests, and deletes any Invites associated with them. TODO Requests for ownership?
 */
public class UserAddToGroupOwners {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String groupID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Group group = Group.readGroup(groupID);

        // Make sure they aren't already inside the event
        if (group.owners.contains(userID)) {
            throw new Exception("That user is already in the group!");
        }

        if (fromID == null || (!group.owners.contains(fromID) && !Constants.isAdmin(fromID))) {
            throw new Exception("Only an existing owner can make you an owner!");
        }

        // Add to user's owned groups
        databaseActions.add(UserDatabaseActionBuilder.updateAddOwnedGroup(userID, itemType,
                groupID, false));

        // Add to user's groups
        databaseActions.add(UserDatabaseActionBuilder.updateAddGroup(userID, itemType, groupID, false));

        // Add to group's owners
        databaseActions.add(GroupDatabaseActionBuilder.updateAddOwner(groupID, userID));

        // Add to group's members
        databaseActions.add(GroupDatabaseActionBuilder.updateAddMember(groupID, userID, false));

        // Delete any potential invites associated with this
        User user = User.readUser(userID, itemType);
        for (String inviteID : user.receivedInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(groupID)) {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }
        for (String inviteID : group.receivedInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(userID)) {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }

        return databaseActions;
    }
}
