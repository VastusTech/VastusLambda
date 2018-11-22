package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateDifficulty {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String difficulty) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Event event = Event.readEvent(eventID);
        if (!fromID.equals(event.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update an event that you own!");
        }

        // Test to see if we can actually do this
        if (!event.ifChallenge) {
            throw new Exception("The event needs to be a challenge to do this!");
        }

        // Test the integer
        Integer.parseInt(difficulty);

        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateDifficulty(eventID, difficulty));

        return databaseActions;
    }
}
