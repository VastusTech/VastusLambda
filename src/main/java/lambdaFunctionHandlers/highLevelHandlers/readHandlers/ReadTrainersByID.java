package main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers;

import main.java.databaseObjects.Trainer;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.TrainerResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadTrainersByID {
    public static List<ObjectResponse> handle(String[] trainerIDs) throws Exception {
        List<ObjectResponse> trainerResponses = new ArrayList<>();
        for (String id : trainerIDs) {
            trainerResponses.add(new TrainerResponse(Trainer.readTrainer(id)));
        }
        return trainerResponses;
    }
}
