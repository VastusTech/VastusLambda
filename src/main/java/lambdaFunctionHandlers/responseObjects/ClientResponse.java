package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Client;
import main.java.databaseObjects.TimeInterval;

import java.util.ArrayList;
import java.util.List;

public class ClientResponse extends ObjectResponse {
    private String itemType = "Client";
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

    private String[] friends;
    private String[] friendRequests;

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
