package main.java.lambdaFunctionHandlers.clientFunctionHandlers;

import main.java.databaseObjects.Client;
import main.java.lambdaFunctionHandlers.responseObjects.ClientResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadClientsByUsername {
    public static List<ObjectResponse> handle(String[] usernames) throws Exception {
        List<ObjectResponse> clientResponses = new ArrayList<>();
        for (String username : usernames) {
            clientResponses.add(new ClientResponse(Client.queryClient(username)));
        }
        return clientResponses;
    }
}
