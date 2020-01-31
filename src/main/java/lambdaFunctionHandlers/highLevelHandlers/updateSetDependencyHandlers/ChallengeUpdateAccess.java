package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.logic.ItemType;
import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the access for a Challenge as well as all of the Events it contains.
 */
public class ChallengeUpdateAccess {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String access) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (fromID == null || (!fromID.equals(Challenge.readChallenge(challengeID).owner) && !Constants.isAdmin(fromID))) {
            throw new Exception("PERMISSIONS ERROR: You can only update an event that you own!");
        }

        if (!access.equals("private") && !access.equals("public")) {
            throw new Exception("Event access must be \"public\" or \"private\"!");
        }

        Challenge challenge = Challenge.readChallenge(challengeID);
        if (access.equals("public") && ItemType.getItemType(challenge.owner).equals("Client")) {
            throw new Exception("A Client cannot own a public Challenge!");
        }

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateAccess(challengeID, access));

        // Update the access for all the challenge's events too!
        for (String eventID : challenge.events) {
            databaseActions.addAll(EventUpdateAccess.getActions(fromID, eventID, access));
        }

        return databaseActions;
    }
}
