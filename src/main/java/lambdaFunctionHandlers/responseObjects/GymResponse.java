package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Gym;
import main.java.databaseObjects.TimeInterval;

import java.util.ArrayList;
import java.util.List;

public class GymResponse extends ObjectResponse {
    public String itemType = "Gym";
    public String name;
    public String foundingDay;
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

    public String address;
    public String[] trainerIDs;
    public String[] weeklyHours;
    public String[] vacationTimes;
    public String sessionCapacity;
    public String gymType;
    public String paymentSplit;

    public GymResponse(Gym gym) {
        super(gym.id);
        this.name = gym.name;
        this.foundingDay = gym.birthday;
        this.email = gym.email;
        this.username = gym.username;
        this.profileImagePath = gym.profileImagePath;
        this.scheduledWorkouts = gym.scheduledWorkouts.toArray(new String[]{});
        this.completedWorkouts = gym.completedWorkouts.toArray(new String[]{});
        List<String> scheduledWorkoutTimes = new ArrayList<>();
        for (TimeInterval timeInterval : gym.scheduledWorkoutTimes) {
            scheduledWorkoutTimes.add(timeInterval.toString());
        }
        this.scheduledWorkoutTimes = scheduledWorkoutTimes.toArray(new String[]{});
        List<String> completedWorkoutTimes = new ArrayList<>();
        for (TimeInterval timeInterval : gym.completedWorkoutTimes) {
            completedWorkoutTimes.add(timeInterval.toString());
        }
        this.completedWorkoutTimes = completedWorkoutTimes.toArray(new String[]{});
        this.reviewsBy = gym.reviewsBy.toArray(new String[]{});
        this.reviewsAbout = gym.reviewsAbout.toArray(new String[]{});
        this.friendlinessRating = Float.toString(gym.friendlinessRating);
        this.effectivenessRating = Float.toString(gym.effectivenessRating);
        this.reliabilityRating = Float.toString(gym.reliabilityRating);
        this.bio = gym.bio;
        this.address = gym.address;
        this.trainerIDs = gym.trainerIDs.toArray(new String[]{});
        List<String> weeklyHours = new ArrayList<>();
        for (TimeInterval timeInterval : gym.weeklyHours) {
            weeklyHours.add(timeInterval.toString());
        }
        this.weeklyHours = weeklyHours.toArray(new String[]{});
        List<String> vacationTimes = new ArrayList<>();
        for (TimeInterval timeInterval : gym.vacationTimes) {
            vacationTimes.add(timeInterval.toString());
        }
        this.vacationTimes = vacationTimes.toArray(new String[]{});
        this.sessionCapacity = Integer.toString(gym.sessionCapacity);
        this.gymType = gym.gymType;
        this.paymentSplit = Float.toString(gym.paymentSplit);
    }
}
