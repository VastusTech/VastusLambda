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
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledParty(party.ownerID, partyID));
        databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(party.ownerID, party.time.toString
                ()));
        // remove from each member's fields
        for (String memberID : party.memberIDs) {
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledChallenge(memberID, partyID));
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveScheduledTime(memberID, party.time.toString()));
        }

        // Delete the party
        databaseActions.add(PartyDatabaseActionBuilder.delete(partyID));

        return databaseActions;
    }
}
