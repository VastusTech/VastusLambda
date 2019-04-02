package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Group;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GroupDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the Group that the Challenge is a part of.
 */
public class ChallengeUpdateGroup {
    public static List<DatabaseAction> getActions(String fromID, String challengeID, String groupID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Challenge challenge = Challenge.readChallenge(challengeID);
        Group group = null;
        if (groupID != null) {
            group = Group.readGroup(groupID);
        }

        if (!fromID.equals(challenge.owner) && (group == null || !group.owners.contains(fromID)) && !fromID.equals(Constants
                .adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a challenge that you own!");
        }

        // TODO Is this sufficient?
        if (challenge.group != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateRemoveChallenge(challenge.group, challengeID));
        }
        if (groupID != null) {
            databaseActions.add(GroupDatabaseActionBuilder.updateAddChallenge(groupID, challengeID, false));
        }

        // Get all the actions for this process
        databaseActions.add(ChallengeDatabaseActionBuilder.updateGroup(challengeID, groupID));

        return databaseActions;
    }
}
