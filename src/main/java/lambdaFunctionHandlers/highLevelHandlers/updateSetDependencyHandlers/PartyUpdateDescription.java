package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PartyDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class PartyUpdateDescription {
    public static List<DatabaseAction> getActions(String partyID, String description) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        databaseActions.add(PartyDatabaseActionBuilder.updateTitle(partyID, description));

        return databaseActions;
    }
}
