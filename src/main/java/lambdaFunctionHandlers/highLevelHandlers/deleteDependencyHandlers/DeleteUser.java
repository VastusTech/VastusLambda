package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Group;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.CommentDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PostDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.SubmissionDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.logic.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes a User from the database and also deletes the user ID from most dependencies of their
 * user ID.
 */
public class DeleteUser {
    public static List<DatabaseAction> getActions(String fromID, User user) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // public Set<String> scheduledWorkouts;
        // TODO Revisit
        // public Set<String> completedWorkouts;
        // TODO Revisit
        // public Set<String> reviewsBy;
        // TODO Revisit
        // public Set<String> reviewsAbout;
        // TODO Revisit

        // Remove from all friend's friends lists
        for (String friendID : user.friends) {
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveFriend(friendID, user.itemType, user.id));
        }

        // Also remove from challenges
        for (String challengeID : user.challenges) {
            databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveMember(challengeID, user.id));
        }

        // public Set<String> completedChallenges;
        // Don't ever change a completed Challenge.

        // Also delete the challenges you own
        for (String challengeID : user.ownedChallenges) {
            databaseActions.addAll(DeleteChallenge.getActions(fromID, challengeID));
        }

        // public Set<String> challengesWon;
        // Don't change the winner of a completed challenge

        // Also remove from scheduled events
        for (String eventID : user.scheduledEvents) {
            databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, user.id));
        }
        // public Set<String> completedEvents;
        // Don't ever change a completed event.

        // Also delete the events you own
        for (String eventID : user.ownedEvents) {
            databaseActions.addAll(DeleteEvent.getActions(fromID, eventID));
        }

        // Remove all invites from sent
        for (String inviteID : user.sentInvites) {
            databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
        }

        // Remove all invites from received
        for (String inviteID : user.receivedInvites) {
            databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
        }

        // public Set<String> posts;
        // Don't delete any posts

        // public Set<String> submissions;
        // Don't delete any submissions

        // Delete from any liked posts or submissions
        for (String likeID : user.liked) {
            String likeItemType = ItemType.getItemType(likeID);
            if (likeItemType.equals("Post")) {
                databaseActions.add(PostDatabaseActionBuilder.updateRemoveLike(likeID, user.id));
            }
            else if (likeItemType.equals("Submission")) {
                databaseActions.add(SubmissionDatabaseActionBuilder.updateRemoveLike(likeID, user.id));
            }
            else if (likeItemType.equals("Comment")) {
                databaseActions.add(CommentDatabaseActionBuilder.updateRemoveLike(likeID, user.id));
            }
            else {
                throw new Exception("Could not use like ID of item type = " + likeItemType);
            }
        }

        // public Set<String> comments;
        // Don't delete any comments

        // Remove yourself as a member from every group
        for (String groupID : user.groups) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveMember(groupID, user.id));
        }

        // For every group either remove yourself as an owner, or delete it entirely if no other owners
        // TODO Delete if empty
        for (String groupID : user.ownedGroups) {
            Group group = Group.readGroup(groupID);
            if (group.owners.size() == 1 && group.owners.contains(user.id)) {
                databaseActions.addAll(DeleteGroup.getActions(fromID, groupID));
            }
            else {
                databaseActions.add(GroupDatabaseActionBuilder.updateRemoveOwner(groupID, user.id));
            }
        }

        // Delete all streaks as well
        for (String streakID : user.streaks) {
            databaseActions.addAll(DeleteStreak.getActions(fromID, streakID));
        }

        return databaseActions;
    }
}
