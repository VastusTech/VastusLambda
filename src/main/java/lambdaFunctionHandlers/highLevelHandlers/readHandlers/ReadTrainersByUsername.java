package main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers;

import main.java.databaseObjects.Trainer;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.TrainerResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadTrainersByUsername {
    public static List<ObjectResponse> handle(String[] usernames) throws Exception {
        List<ObjectResponse> trainerResponses = new ArrayList<>();
        for (String username : usernames) {
            trainerResponses.add(new TrainerResponse(Trainer.queryTrainer(username)));
        }
        return trainerResponses;
    }
}
