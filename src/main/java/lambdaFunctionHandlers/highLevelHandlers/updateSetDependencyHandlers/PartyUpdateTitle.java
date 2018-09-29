package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PartyDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class PartyUpdateTitle {
    public static List<DatabaseAction> getActions(String partyID, String title) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        databaseActions.add(PartyDatabaseActionBuilder.updateTitle(partyID, title));

        return databaseActions;
    }
}
