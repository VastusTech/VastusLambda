package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventUpdateAccess {
    public static List<DatabaseAction> getActions(String fromID, String eventID, String access) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(Event.readEvent(eventID).owner) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only update an event that you own!");
        }

        if (!access.equals("private") && !access.equals("public")) {
            throw new Exception("Challenge access must be \"public\" or \"private\"!");
        }



        // Get all the actions for this process
        databaseActions.add(EventDatabaseActionBuilder.updateAccess(eventID, access));

        return databaseActions;
    }
}
