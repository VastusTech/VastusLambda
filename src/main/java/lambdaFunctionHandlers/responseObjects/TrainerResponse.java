package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseObjects.Trainer;

import java.util.ArrayList;
import java.util.List;

public class TrainerResponse extends ObjectResponse {
    private String itemType = "Trainer";
    private String name;
    private String gender;
    private String birthday;
    private String email;
    private String username;
    private String profileImagePath;
    private String[] scheduledWorkouts;
    private String[] completedWorkouts;
    private String[] scheduledWorkoutTimes;
    private String[] completedWorkoutTimes;
    private String[] reviewsBy;
    private String[] reviewsAbout;
    private String friendlinessRating;
    private String effectivenessRating;
    private String reliabilityRating;
    private String bio;

    private String gymID;
    private String[] availableTimes;
    private String workoutSticker;
    private String preferredIntensity;
    private String workoutCapacity;
    private String workoutPrice;

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
