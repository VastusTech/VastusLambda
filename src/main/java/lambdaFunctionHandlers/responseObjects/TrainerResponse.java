package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseObjects.Trainer;

import java.util.ArrayList;
import java.util.List;

public class TrainerResponse extends ObjectResponse {
    public String itemType = "Trainer";
    public String name;
    public String gender;
    public String birthday;
    public String email;
    public String username;
    public String profileImagePath;
    public String[] scheduledWorkouts;
    public String[] completedWorkouts;
    public String[] scheduledWorkoutTimes;
    public String[] completedWorkoutTimes;
    public String[] reviewsBy;
    public String[] reviewsAbout;
    public String friendlinessRating;
    public String effectivenessRating;
    public String reliabilityRating;
    public String bio;

    public String gymID;
    public String[] availableTimes;
    public String workoutSticker;
    public String preferredIntensity;
    public String workoutCapacity;
    public String workoutPrice;

    public TrainerResponse(Trainer trainer) {
        super(trainer.id);
        this.name = trainer.name;
        this.gender = trainer.gender;
        this.birthday = trainer.birthday;
        this.email = trainer.email;
        this.username = trainer.username;
        this.profileImagePath = trainer.profileImagePath;
        this.scheduledWorkouts = trainer.scheduledWorkouts.toArray(new String[]{});
        this.completedWorkouts = trainer.completedWorkouts.toArray(new String[]{});
        List<String> scheduledWorkoutTimes = new ArrayList<>();
        for (TimeInterval timeInterval : trainer.scheduledWorkoutTimes) {
            scheduledWorkoutTimes.add(timeInterval.toString());
        }
        this.scheduledWorkoutTimes = scheduledWorkoutTimes.toArray(new String[]{});
        List<String> completedWorkoutTimes = new ArrayList<>();
        for (TimeInterval timeInterval : trainer.completedWorkoutTimes) {
            completedWorkoutTimes.add(timeInterval.toString());
        }
        this.completedWorkoutTimes = completedWorkoutTimes.toArray(new String[]{});
        this.reviewsBy = trainer.reviewsBy.toArray(new String[]{});
        this.reviewsAbout = trainer.reviewsAbout.toArray(new String[]{});
        this.friendlinessRating = Float.toString(trainer.friendlinessRating);
        this.effectivenessRating = Float.toString(trainer.effectivenessRating);
        this.reliabilityRating = Float.toString(trainer.reliabilityRating);
        this.bio = trainer.bio;
        this.gymID = trainer.gymID;
        List<String> availableTimes = new ArrayList<>();
        for (TimeInterval timeInterval : trainer.availableTimes) {
            availableTimes.add(timeInterval.toString());
        }
        this.availableTimes = availableTimes.toArray(new String[]{});
        this.workoutSticker = trainer.workoutSticker;
        this.preferredIntensity = trainer.preferredIntensity;
        this.workoutCapacity = Integer.toString(trainer.workoutCapacity);
        this.workoutPrice = Integer.toString(trainer.workoutPrice);
    }
}