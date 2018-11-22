package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Workout;

public class WorkoutResponse extends ObjectResponse {
    public String itemType = "Workout";
    public String time;
    public String ifCompleted;
    public String trainer;
    public String[] clients;
    public String capacity;
    public String gym;
    public String sticker;
    public String intensity;
    public String[] missingReviews;
    public String price;

    public WorkoutResponse(Workout workout) {
        super(workout.id);
        this.time = workout.time.toString();
        this.ifCompleted = Boolean.toString(workout.ifCompleted);
        this.trainer = workout.trainer;
        this.clients = workout.clients.toArray(new String[]{});
        this.capacity = Integer.toString(workout.capacity);
        this.gym = workout.gym;
        this.sticker = workout.sticker;
        this.intensity = workout.intensity;
        this.missingReviews = workout.missingReviews.toArray(new String[]{});
        this.price = Integer.toString(workout.price);
    }
}
