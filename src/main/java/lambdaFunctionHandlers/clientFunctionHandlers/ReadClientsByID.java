package main.java.lambdaFunctionHandlers.clientFunctionHandlers;

import main.java.databaseObjects.Client;
import main.java.lambdaFunctionHandlers.responseObjects.ClientResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadClientsByID {
    public static List<ObjectResponse> handle(String[] clientIDs) throws Exception {
        List<ObjectResponse> clientResponses = new ArrayList<>();
        for (String id : clientIDs) {
            clientResponses.add(new ClientResponse(Client.readClient(id)));
        }
        return clientResponses;
    }
}
