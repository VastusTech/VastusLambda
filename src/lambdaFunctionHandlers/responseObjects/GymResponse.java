package lambdaFunctionHandlers.responseObjects;

import databaseObjects.Gym;
import databaseObjects.TimeInterval;

import java.util.ArrayList;
import java.util.List;

public class GymResponse extends ObjectResponse {
    private String itemType = "Gym";
    private String name;
    private String foundingDay;
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

    private String address;
    private String[] trainerIDs;
    private String[] weeklyHours;
    private String[] vacationTimes;
    private String sessionCapacity;
    private String gymType;
    private String paymentSplit;

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
