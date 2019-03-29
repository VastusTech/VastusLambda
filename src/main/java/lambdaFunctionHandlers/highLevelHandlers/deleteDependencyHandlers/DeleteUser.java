package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Group;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class DeleteUser {
    public static List<DatabaseAction> getActions(String fromID, User user) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        // TODO We don't want to give people the power of deleting reviews because they're not there
        // Remove reviews from reviews by and reviews about
//        for (String reviewID : user.reviewsBy) {
//            // Get all the actions that would be necessary for the review deleting process
//            databaseActions.addAll(DeleteReview.getActions(fromID, reviewID));
//        }
//        for (String reviewID : user.reviewsAbout) {
//            // Get all the actions that would be necessary for the review deleting process
//            databaseActions.addAll(DeleteReview.getActions(fromID, reviewID));
//        }


        // Also remove from scheduled events
        for (String eventID : user.scheduledEvents) {
            databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, user.id));
        }

        // Also remove from challenges
        for (String challengeID : user.challenges) {
            databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveMember(challengeID, user.id));
        }

        // Also remove from completed events
//        for (String eventID : user.completedEvents) {
//            databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, user.id));
//        }

        // Also delete the events you own
        for (String eventID : user.ownedEvents) {
            databaseActions.addAll(DeleteEvent.getActions(fromID, eventID));
        }

        // Also delete the challenges you own
        for (String challengeID : user.ownedChallenges) {
            databaseActions.addAll(DeleteChallenge.getActions(fromID, challengeID));
        }

        // Remove yourself as a member from every group
        for (String groupID : user.groups) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveMember(groupID, user.id));
        }

        // For every group either remove yourself as an owner, or delete it entirely if no other owners
        for (String groupID : user.ownedGroups) {
            Group group = Group.readGroup(groupID);
            if (group.owners.size() == 1 && group.owners.contains(user.id)) {
                databaseActions.addAll(DeleteGroup.getActions(fromID, groupID));
            }
            else {
                databaseActions.add(GroupDatabaseActionBuilder.updateRemoveOwner(groupID, user.id));
            }
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

        // Delete all streaks as well
        for (String streakID : user.streaks) {
            databaseActions.addAll(DeleteStreak.getActions(fromID, streakID));
        }

        return databaseActions;
    }
}
