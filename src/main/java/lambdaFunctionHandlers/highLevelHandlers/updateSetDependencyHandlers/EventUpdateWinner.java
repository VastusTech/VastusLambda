package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.UserDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateWinner {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String winner) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Event challenge = Event.readEvent(eventID);

        if (!challenge.owner.equals(fromID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: In order to update an event, you need to be the owner!");
        }

        if (!challenge.ifChallenge) {
            throw new Exception("To win an event, that event needs to be a challenge!");
        }

        String winnerItemType = ItemType.getItemType(winner);

        // Set the winner and update their challenges won
        databaseActions.add(EventDatabaseActionBuilder.updateWinner(eventID, winner));
        databaseActions.add(UserDatabaseActionBuilder.updateAddChallengeWon(winner, winnerItemType, eventID));

        //  Finally remove the challenge from everyone's scheduled and into their completed
        for (String userID : challenge.members) {
            String userItemType = ItemType.getItemType(userID);
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledEvent(userID, userItemType, eventID));
            databaseActions.add(UserDatabaseActionBuilder.updateRemoveScheduledTime(userID, userItemType, challenge
                    .time.toString()));
            databaseActions.add(UserDatabaseActionBuilder.updateAddCompletedEvent(userID, userItemType, eventID));
        }

        // Set the event if completed to true
        databaseActions.add(EventDatabaseActionBuilder.updateIfCompleted(eventID, "true"));

        return databaseActions;
    }
}
