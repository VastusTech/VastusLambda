package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.databaseOperations.exceptions.ItemNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes the Challenge from the database and from any dependencies on its Challenge ID.
 */
public class DeleteChallenge {
    public static List<DatabaseAction> getActions(String fromID, String challengeID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Challenge challenge = Challenge.readChallenge(challengeID);

        if (!fromID.equals(challenge.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete an challenge you own!");
        }

        // remove from owner's fields
        String ownerItemType = ItemType.getItemType(challenge.owner);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveOwnedChallenge(challenge.owner, ownerItemType, challengeID));

        // remove from each member's fields
        for (String member : challenge.members) {
            String memberItemType = ItemType.getItemType(member);
            User user = User.readUser(member, memberItemType);

            if (!challenge.ifCompleted) {
                // Remove the challenge if it's not completed
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveChallenge(member, memberItemType, challengeID));
            }
            else {
                // Remove the completed challenge if it is completed
                databaseActions.add(UserDatabaseActionBuilder.updateRemoveCompletedChallenge(member, memberItemType,
                        challengeID));
            }

            // Also check their sentInvites and check to see if they sent any invites for this challenge
            for (String inviteID : user.sentInvites) {
                try {
                    Invite invite = Invite.readInvite(inviteID);
                    if (invite.about.equals(challengeID)) {
                        databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
                    }
                }
                catch (ItemNotFoundException e) {
                    Constants.debugLog("Couldn't find invite in user's sent invites, " +
                            "but it's not really our problem...");
                }
            }
        }

        // Delete all the receivedInvites
        for (String inviteID : challenge.receivedInvites) {
            try {
                databaseActions.addAll(DeleteInvite.getActions(fromID, inviteID));
            }
            catch (ItemNotFoundException e) {
                Constants.debugLog("Couldn't delete invite, but it's okay");
            }
        }

        // Remove from the events and from the group
        for (String eventID : challenge.events) {
            // TODO Instead of deleting maybe we can just complete them?
            databaseActions.addAll(DeleteEvent.getActions(fromID, eventID));
        }

        // Remove from the completed events
        for (String eventID : challenge.completedEvents) {
            databaseActions.addAll(DeleteEvent.getActions(fromID, eventID));
        }

        // Remove from the potential group
        if (challenge.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveChallenge(challenge.group, challengeID));
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveCompletedChallenge(challenge.group, challengeID));
        }

        // Remove from the winner's challenge
        if (challenge.winner != null) {
            String winnerType = ItemType.getItemType(challenge.winner);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveChallengeWon(challenge.winner,
                    winnerType, challengeID));
        }

        // Delete all the submissions
        for (String submissionID : challenge.submissions) {
            databaseActions.addAll(DeleteSubmission.getActions(fromID, submissionID));
        }

        // Delete all the streaks
        for (String streakID : challenge.streaks) {
            databaseActions.addAll(DeleteStreak.getActions(fromID, streakID));
        }

        // Delete the challenge
        databaseActions.add(ChallengeDatabaseActionBuilder.delete(challengeID));

        return databaseActions;
    }
}
