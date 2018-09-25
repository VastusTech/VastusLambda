package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Workout;

public class WorkoutResponse extends ObjectResponse {
    public String itemType = "Workout";
    public String time;
    public String trainerID;
    public String[] clientIDs;
    public String capacity;
    public String gymID;
    public String sticker;
    public String intensity;
    public String[] missingReviews;
    public String price;

    public WorkoutResponse(Workout workout) {
        super(workout.id);
        this.time = workout.time.toString();
        this.trainerID = workout.trainerID;
        this.clientIDs = workout.clientIDs.toArray(new String[]{});
        this.capacity = Integer.toString(workout.capacity);
        this.gymID = workout.gymID;
        this.sticker = workout.sticker;
        this.intensity = workout.intensity;
        this.missingReviews = workout.missingReviews.toArray(new String[]{});
        this.price = Integer.toString(workout.price);
    }
}
