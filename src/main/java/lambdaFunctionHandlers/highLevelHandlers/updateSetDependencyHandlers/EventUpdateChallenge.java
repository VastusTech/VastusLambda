package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the Challenge that an Event is associated with.
 */
public class EventUpdateChallenge {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String challengeID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO Revisit
        // Get the event
        Event event = Event.readEvent(eventID);
        Challenge challenge = null;
        if (challengeID != null) {
            challenge = Challenge.readChallenge(challengeID);
        }

        if (fromID == null || (!fromID.equals(event.owner) && ((challenge == null
                || fromID.equals(challenge.owner))) && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only update an event that you own!");
        }

        // Switch out the challenge's events
        if (event.challenge != null) {
            databaseActions.add(ChallengeDatabaseActionBuilder.updateRemoveEvent(challengeID, event.challenge));
        }
        if (challengeID != null) {
            databaseActions.add(ChallengeDatabaseActionBuilder.updateAddEvent(challengeID, eventID, false));
        }

        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateChallenge(eventID, challengeID));

        return databaseActions;
    }
}
