package main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers;

import main.java.databaseObjects.Gym;
import main.java.lambdaFunctionHandlers.responseObjects.GymResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadGymsByID {
    public static List<ObjectResponse> handle(String[] gymIDs) throws Exception {
        List<ObjectResponse> gymResponses = new ArrayList<>();
        for (String id : gymIDs) {
            gymResponses.add(new GymResponse(Gym.readGym(id)));
        }
        return gymResponses;
    }
}
