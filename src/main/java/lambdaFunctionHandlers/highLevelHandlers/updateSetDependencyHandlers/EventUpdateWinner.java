package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateWinner {
    public static List<DatabaseAction> getActions(String eventID, String winner) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Event challenge = Event.readEvent(eventID);

        if (!challenge.ifChallenge) {
            throw new Exception("To win an event, that event needs to be a challenge!");
        }

        if (!challenge.members.contains(winner)) {
            throw new Exception("Winner must be a member of the challenge!");
        }

        if (!challenge.time.hasAlreadyFinished()) {
            throw new Exception("You cannot complete the challenge if it hasn't already finished!");
        }

        // Set the winner and update their challenges won
        databaseActions.add(EventDatabaseActionBuilder.updateWinner(eventID, winner));
        databaseActions.add(ClientDatabaseActionBuilder.updateAddChallengeWon(winner, eventID));

        //  Finally remove the challenge from everyone's scheduled and into their completed
        for (String clientID : challenge.members) {
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledEvent(clientID, eventID));
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(clientID, challenge.time.toString()));
            databaseActions.add(ClientDatabaseActionBuilder.updateAddCompletedEvent(clientID, eventID));
        }

        return databaseActions;
    }
}
