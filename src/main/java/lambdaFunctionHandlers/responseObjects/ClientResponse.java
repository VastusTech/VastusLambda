package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Client;
import main.java.databaseObjects.TimeInterval;

import java.util.ArrayList;
import java.util.List;

public class ClientResponse extends ObjectResponse {
    public String item_type = "Client";
    public String name;
    public String gender;
    public String birthday;
    public String email;
    public String username;
    public String profileImagePath;
    public String[] scheduledWorkouts;
    public String[] completedWorkouts;
    public String[] scheduledTimes;
    public String[] reviewsBy;
    public String[] reviewsAbout;
    public String friendlinessRating;
    public String effectivenessRating;
    public String reliabilityRating;
    public String bio;

    public String[] friends;
    public String[] friendRequests;
    public String challengesWon;
    public String[] scheduledParties;
    public String[] completedParties;
    public String[] ownedParties;
    public String[] scheduledChallenges;
    public String[] completedChallenges;
    public String[] ownedChallenges;

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
        List<String> scheduledTimes = new ArrayList<>();
        for (TimeInterval timeInterval : client.scheduledTimes) {
            scheduledTimes.add(timeInterval.toString());
        }
        this.scheduledTimes = scheduledTimes.toArray(new String[]{});
        this.reviewsBy = client.reviewsBy.toArray(new String[]{});
        this.reviewsAbout = client.reviewsAbout.toArray(new String[]{});
        this.friendlinessRating = Float.toString(client.friendlinessRating);
        this.effectivenessRating = Float.toString(client.effectivenessRating);
        this.reliabilityRating = Float.toString(client.reliabilityRating);
        this.bio = client.bio;
        this.friends = client.friends.toArray(new String[]{});
        this.friendRequests = client.friendRequests.toArray(new String[]{});
        this.challengesWon = Integer.toString(client.challengesWon);
        this.scheduledParties = client.scheduledParties.toArray(new String[]{});
        this.completedParties = client.completedParties.toArray(new String[]{});
        this.ownedParties = client.ownedParties.toArray(new String[]{});
        this.scheduledChallenges = client.scheduledChallenges.toArray(new String[]{});
        this.completedChallenges = client.completedChallenges.toArray(new String[]{});
        this.ownedChallenges = client.ownedChallenges.toArray(new String[]{});
    }
}
