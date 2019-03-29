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
 * TODO
 */
public class UserAddToGroup {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String groupID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Group group = Group.readGroup(groupID);

        // Make sure they aren't already inside the event
        if (group.members.contains(userID)) {
            throw new Exception("That user is already in the group!");
        }

        boolean ifAcceptingRequest = false;

        if (group.memberRequests.contains(userID)) {
            if (!group.owners.contains(fromID) && !fromID.equals(Constants.adminKey)) {
                throw new Exception("PERMISSIONS ERROR: Only the owner can accept your event member request!");
            }
            else {
                ifAcceptingRequest = true;
                databaseActions.add(GroupDatabaseActionBuilder.updateRemoveMemberRequest(groupID, userID));
            }
        }
        else {
            if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
                throw new Exception("PERMISSIONS ERROR: Only you can add you to an event!");
            }
        }

        // Add to user's groups
        databaseActions.add(UserDatabaseActionBuilder.updateAddGroup(userID, itemType,
                groupID, false));

        // Add to event's members
        databaseActions.add(GroupDatabaseActionBuilder.updateAddMember(groupID, userID, ifAcceptingRequest));

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
