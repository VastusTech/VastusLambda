package lambdaFunctionHandlers.responseObjects;

import databaseObjects.Workout;

public class WorkoutResponse extends ObjectResponse {
    private String itemType = "Workout";
    private String time;
    private String trainerID;
    private String[] clientIDs;
    private String capacity;
    private String gymID;
    private String sticker;
    private String intensity;
    private String[] missingReviews;
    private String price;

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
