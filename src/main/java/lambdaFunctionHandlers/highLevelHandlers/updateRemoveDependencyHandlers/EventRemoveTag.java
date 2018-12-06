package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventRemoveTag {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String tag) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Event event = Event.readEvent(eventID);

        if (!fromID.equals(event.owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update a gym you own!");
        }

        databaseActions.add(EventDatabaseActionBuilder.updateRemoveTag(eventID, tag));

        return databaseActions;
    }
}
