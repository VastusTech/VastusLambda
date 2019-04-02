package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteInvite;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a Friend directly to a User and vice versa, checking to make sure that the User actually has
 * the Friend in their requests already.
 */
public class UserAddFriend {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String friendID)
            throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // First find the invite where this cam from
        String userItemType = ItemType.getItemType(userID);
        User user = User.readUser(userID, userItemType);

        Invite invite = null;

        for (String inviteID : user.receivedInvites) {
            Invite receivedInvite = Invite.readInvite(inviteID);
            if (receivedInvite.about.equals(friendID)) {
                invite = receivedInvite;
            }
        }

        if (invite == null) {
            throw new Exception("Need to have a friend request to add a friend directly!");
        }

        if (!invite.inviteType.equals("friendRequest")) {
            throw new Exception("Cannot accept a invite as a friendRequest that isn't one, clearly...");
        }

        if (!fromID.equals(userID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only accept friend requests for yourself!");
        }

        // Add the friend to your own friends list
        databaseActions.add(UserDatabaseActionBuilder.updateAddFriend(userID, itemType, friendID, true));

        // Mutual friendships
        String friendItemType = ItemType.getItemType(friendID);
        databaseActions.add(UserDatabaseActionBuilder.updateAddFriend(friendID, friendItemType, userID, false));

        // This will be done in the delete invite handler
        // Remove the friend request before
        // databaseActions.add(UserDatabaseActionBuilder.updateRemoveFriendRequest(userID, itemType, invite.about));

        // Delete the invite after we're done with it!
        databaseActions.addAll(DeleteInvite.getActions(fromID, invite.id));

        return databaseActions;
    }
}
