package main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Client;
import main.java.lambdaFunctionHandlers.responseObjects.ClientResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadClientsByID {
    public static List<ObjectResponse> handle(String[] clientIDs) throws Exception {
        List<ObjectResponse> clientResponses = new ArrayList<>();
        for (String id : clientIDs) {
            Client client = Client.readClient(id);
            Constants.debugLog("Read client id: " + client.id + "\n");
            ClientResponse clientResponse = new ClientResponse(client);
            clientResponses.add(clientResponse);
            // clientResponses.add(new ClientResponse(Client.readClient(id)));
        }
        return clientResponses;
    }
}
