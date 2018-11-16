package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.ItemType;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteUser {
    public static List<DatabaseAction> getActions(String fromID, User user) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Remove reviews from reviews by and reviews about
        for (String reviewID : user.reviewsBy) {
            // Get all the actions that would be necessary for the review deleting process
            databaseActions.addAll(DeleteReview.getActions(fromID, reviewID));
        }
        for (String reviewID : user.reviewsAbout) {
            // Get all the actions that would be necessary for the review deleting process
            databaseActions.addAll(DeleteReview.getActions(fromID, reviewID));
        }


        // Also remove from scheduled events
        for (String eventID : user.scheduledEvents) {
            databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, user.id));
        }

        // Also remove from completed events
        for (String eventID : user.completedEvents) {
            databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, user.id));
        }

        // Also delete the events you own
        for (String eventID : user.ownedEvents) {
            databaseActions.addAll(DeleteEvent.getActions(user.id, eventID));
        }

        // Remove from all friend's friends lists
        for (String friendID : user.friends) {
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveFriend(friendID, user.itemType, user.id));
        }

        // Remove all invites from sent
        for (String inviteID : user.sentInvites) {
            databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
        }

        // Remove all invites from received
        for (String inviteID : user.receivedInvites) {
            databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
        }

        return databaseActions;
    }
}
