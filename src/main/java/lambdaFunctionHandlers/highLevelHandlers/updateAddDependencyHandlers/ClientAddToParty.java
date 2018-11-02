package main.java.lambdaFunctionHandlers.highLevelHandlers.updateAddDependencyHandlers;

import main.java.databaseObjects.Party;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PartyDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientAddToParty {
    public static List<DatabaseAction> getActions(String clientID, String partyID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        // Get all the actions for this process
        Party party = Party.readParty(partyID);

        // Add to client's scheduled workouts
        databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledParty(clientID,
                partyID, false));
        // Add to client's scheduled workout times
        databaseActions.add(ClientDatabaseActionBuilder.updateAddScheduledTime(clientID, party.time.toString
                ()));
        // Add to workout's clients
        databaseActions.add(PartyDatabaseActionBuilder.updateAddMember(partyID, clientID));

        return databaseActions;
    }
}
