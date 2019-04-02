package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

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
 * Removes a User from a Group and deletes any potential Invites the User sent for the Group.
 */
public class UserRemoveFromGroup {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String groupID) throws
            Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Group group = Group.readGroup(groupID);

        if (!fromID.equals(userID) && !group.owners.contains(fromID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only remove someone if you're the owner or that someone " +
                    "is you!");
        }

        if (!group.members.contains(userID)) {
            throw new Exception("You can't remove a user that isn't already in the group!");
        }

        if (group.owners.contains(userID)) {
            throw new Exception("You can't remove an owner from the group! Just delete the group in that case!");
        }

        // We delete the group from ourselves
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveGroup(userID, itemType, groupID));

        // We also remove any invites that pertain to this group we may have sent
        User user = User.readUser(userID, itemType);
        for (String inviteID : user.sentInvites) {
            Invite invite = Invite.readInvite(inviteID);
            if (invite.about.equals(groupID)) {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
        }

        // And we delete ourselves from the challenge
        databaseActions.add(GroupDatabaseActionBuilder.updateRemoveMember(groupID, userID));

        return databaseActions;
    }
}
