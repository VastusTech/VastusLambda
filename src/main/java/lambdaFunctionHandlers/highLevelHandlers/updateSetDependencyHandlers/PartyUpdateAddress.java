package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PartyDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class PartyUpdateAddress {
    public static List<DatabaseAction> getActions(String partyID, String address) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // TODO CHeck the address for validity?

        // Get all the actions for this process
        databaseActions.add(PartyDatabaseActionBuilder.updateAddress(partyID, address));

        return databaseActions;
    }
}
