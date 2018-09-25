package main.java.lambdaFunctionHandlers.gymFunctionHandlers;

import main.java.databaseObjects.DatabaseObject;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadAllGyms {
    public static List<ObjectResponse> handle() throws Exception {
        List<ObjectResponse> objectResponses = new ArrayList<>();
        for (DatabaseObject databaseObject : DynamoDBHandler.getInstance().getAll("Gym")) {
            objectResponses.add(databaseObject.getResponse());
        }
        return objectResponses;
    }
}
