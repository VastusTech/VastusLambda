package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Client;
import main.java.databaseObjects.TimeInterval;

import java.util.ArrayList;
import java.util.List;

public class ClientResponse extends ObjectResponse {
    public String itemType = "Client";
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

    public String[] friends;
    public String[] friendRequests;

    public ClientResponse(Client client) {
        super(client.id);
        this.name = client.name;
        this.gender = client.gender;
        this.birthday = client.birthday;
        this.email = client.email;
        this.username = client.username;
        this.profileImagePath = client.profileImagePath;
        this.scheduledWorkouts = client.scheduledWorkouts.toArray(new String[]{});
        this.completedWorkouts = client.completedWorkouts.toArray(new String[]{});
        List<String> scheduledWorkoutTimes = new ArrayList<>();
        for (TimeInterval timeInterval : client.scheduledWorkoutTimes) {
            scheduledWorkoutTimes.add(timeInterval.toString());
        }
        this.scheduledWorkoutTimes = scheduledWorkoutTimes.toArray(new String[]{});
        List<String> completedWorkoutTimes = new ArrayList<>();
        for (TimeInterval timeInterval : client.completedWorkoutTimes) {
            completedWorkoutTimes.add(timeInterval.toString());
        }
        this.completedWorkoutTimes = completedWorkoutTimes.toArray(new String[]{});
        this.reviewsBy = client.reviewsBy.toArray(new String[]{});
        this.reviewsAbout = client.reviewsAbout.toArray(new String[]{});
        this.friendlinessRating = Float.toString(client.friendlinessRating);
        this.effectivenessRating = Float.toString(client.effectivenessRating);
        this.reliabilityRating = Float.toString(client.reliabilityRating);
        this.bio = client.bio;
        this.friends = client.friends.toArray(new String[]{});
        this.friendRequests = client.friendRequests.toArray(new String[]{});
    }
}
