package main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Party;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.PartyResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadPartiesByID {
    public static List<ObjectResponse> handle(String[] partyIDs) throws Exception {
        List<ObjectResponse> partyResponses = new ArrayList<>();
        for (String id : partyIDs) {
            Party party = Party.readParty(id);
            Constants.debugLog("Read party id: " + party.id + "\n");
            PartyResponse partyResponse = new PartyResponse(party);
            partyResponses.add(partyResponse);
        }
        return partyResponses;
    }
}
