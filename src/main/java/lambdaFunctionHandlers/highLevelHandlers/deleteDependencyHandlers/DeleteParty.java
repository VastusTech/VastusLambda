package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Party;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.PartyDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteParty {
    public static List<DatabaseAction> getActions(String partyID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();
        Party party = Party.readParty(partyID);

        // remove from owner's fields
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveOwnedParty(party.owner, partyID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledParty(party.owner, partyID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(party.owner, party.time.toString
                ()));
        // remove from each member's fields
        for (String member : party.members) {
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(member, partyID));
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(member, party.time.toString()));
        }

        // Delete the party
        databaseActions.add(PartyDatabaseActionBuilder.delete(partyID));

        return databaseActions;
    }
}
