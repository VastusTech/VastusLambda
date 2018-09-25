package main.java.lambdaFunctionHandlers.workoutFunctionHandlers;

import main.java.databaseObjects.Workout;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.WorkoutResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadWorkoutsByID {
    public static List<ObjectResponse> handle(String[] workoutIDs) throws Exception {
        List<ObjectResponse> workoutResponses = new ArrayList<>();
        for (String id : workoutIDs) {
            workoutResponses.add(new WorkoutResponse(Workout.readWorkout(id)));
        }
        return workoutResponses;
    }
}
