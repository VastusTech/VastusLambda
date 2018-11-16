package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseObjects.Invite;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteInvite;

import java.util.ArrayList;
import java.util.List;

public class UserAcceptFriendRequest {
    public static List<DatabaseAction> getActions(String fromID, String userID, String itemType, String inviteID)
            throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Invite invite = Invite.readInvite(inviteID);

        if (!invite.inviteType.equals("friendRequest")) {
            throw new Exception("Cannot accept a invite as a friendRequest that isn't one, clearly...");
        }

        if (!fromID.equals(userID) && !fromID.equals("admin")) {
            throw new Exception("PERMISSIONS ERROR: You can only accept friend requests for yourself!");
        }

        // Add the friend to your own friends list
        databaseActions.add(UserDatabaseActionBuilder.updateAddFriend(userID, itemType, invite.about, true));

        // Mutual friendships
        databaseActions.add(UserDatabaseActionBuilder.updateAddFriend(invite.about, itemType, userID, false));

        // This will be done in the delete invite handler
        // Remove the friend request before
        // databaseActions.add(UserDatabaseActionBuilder.updateRemoveFriendRequest(userID, itemType, invite.about));

        // Delete the invite after we're done with it!
        databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));

        return databaseActions;
    }
}
