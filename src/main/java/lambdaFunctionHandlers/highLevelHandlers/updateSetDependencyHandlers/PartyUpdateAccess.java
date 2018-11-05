package main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.PartyDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class PartyUpdateAccess {
    public static List<DatabaseAction> getActions(String partyID, String access) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!access.equals("private") && !access.equals("public")) {
            throw new Exception("Party access must be \"public\" or \"private\"!");
        }

        // Get all the actions for this process
        databaseActions.add(PartyDatabaseActionBuilder.updateAccess(partyID, access));

        return databaseActions;
    }
}

