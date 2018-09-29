package main.java.lambdaFunctionHandlers.highLevelHandlers.updateRemoveDependencyHandlers;

import main.java.databaseObjects.Party;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PartyDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientRemoveFromParty {
    public static List<DatabaseAction> getActions(String clientID, String partyID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Party party = Party.readParty(partyID);

        // We delete the party from ourselves
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledParty(clientID, partyID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(clientID, party.time
                .toString()));

        // And we delete ourselves from the workout
        databaseActions.add(PartyDatabaseActionBuilder.updateRemoveMemberID(partyID, clientID));

        return databaseActions;
    }
}
