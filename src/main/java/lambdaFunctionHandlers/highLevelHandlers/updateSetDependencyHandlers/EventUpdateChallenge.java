package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateChallenge {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String challengeID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get the event
        Event event = Event.readEvent(eventID);
        Challenge challenge = Challenge.readChallenge(challengeID);

        if (!(fromID.equals(event.owner) && fromID.equals(challenge.owner)) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update an event that you own!");
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
