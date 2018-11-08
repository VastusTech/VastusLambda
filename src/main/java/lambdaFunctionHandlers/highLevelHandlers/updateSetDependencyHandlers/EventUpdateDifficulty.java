package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateDifficulty {
    public static List<DatabaseAction> getActions(String eventID, String difficulty) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Test to see if we can actually do this
        if (!Event.readEvent(eventID).ifChallenge) {
            throw new Exception("The event needs to be a challenge to do this!");
        }

        // Test the integer
        Integer.parseInt(difficulty);

        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateDifficulty(eventID, difficulty));

        return databaseActions;
    }
}
