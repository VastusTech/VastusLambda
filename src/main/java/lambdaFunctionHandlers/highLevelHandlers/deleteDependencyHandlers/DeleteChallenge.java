package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.User;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;
import main.java.databaseOperations.exceptions.ItemNotFoundException;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers.EventUpdateChallenge;

import java.util.ArrayList;
import java.util.List;

public class DeleteChallenge {
    public static List<DatabaseAction> getActions(String fromID, String challengeID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Challenge challenge = Challenge.readChallenge(challengeID);

        if (!fromID.equals(challenge.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete an challenge you own!");
        }

        // TODO Should this actually be allowed?
//        if (challenge.ifCompleted) {
//            throw new Exception("Cannot delete a challenge that has already been completed!");
//        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        // remove from owner's fields
        String ownerItemType = ItemType.getItemType(challenge.owner);
        databaseActions.add(UserDatabaseActionBuilder.updateRemoveOwnedChallenge(challenge.owner, ownerItemType, challengeID));

        // remove from each member's fields
        for (String member : challenge.members) {
            String memberItemType = ItemType.getItemType(member);
            User user = User.readUser(member, memberItemType);

            // Remove the challenge
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveChallenge(member, memberItemType, challengeID));

            // Remove the completed challenge as well to cover our bases
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveCompletedChallenge(member, memberItemType,
                    challengeID));

            // Remove the won challenge as well for the same reason
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveChallengeWon(member, memberItemType,
                    challengeID));

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

        // Remove from the events and from the group
        for (String eventID : challenge.events) {
            databaseActions.addAll(EventUpdateChallenge.getActions(fromID, eventID, null));
        }
        if (challenge.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveChallenge(challenge.group, challengeID));
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

        // Delete the challenge
        databaseActions.add(ChallengeDatabaseActionBuilder.delete(challengeID));

        return databaseActions;
    }
}
