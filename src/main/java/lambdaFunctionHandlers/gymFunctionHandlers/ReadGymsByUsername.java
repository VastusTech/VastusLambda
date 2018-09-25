package main.java.lambdaFunctionHandlers.gymFunctionHandlers;

import main.java.databaseObjects.Gym;
import main.java.lambdaFunctionHandlers.responseObjects.GymResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadGymsByUsername {
    public static List<ObjectResponse> handle(String[] usernames) throws Exception {
        List<ObjectResponse> gymResponses = new ArrayList<>();
        for (String username : usernames) {
            gymResponses.add(new GymResponse(Gym.queryGym(username)));
        }
        return gymResponses;
    }
}
