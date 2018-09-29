package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Challenge;

public class ChallengeResponse extends ObjectResponse {
    public String itemType = "Challenge";
    public String ownerID;
    public String time;
    public String[] memberIDs;
    public String capacity;
    public String access;
    public String address;

    public String title;
    public String description;
    public String goal;
    public String difficulty;

    public ChallengeResponse(Challenge challenge) {
        super(challenge.id);
        this.ownerID = challenge.ownerID;
        this.time = challenge.time.toString();
        this.memberIDs = challenge.memberIDs.toArray(new String[]{});
        this.capacity = Integer.toString(challenge.capacity);
        this.access = challenge.access;
        this.address = challenge.address;
        this.title = challenge.title;
        this.description = challenge.description;
        this.goal = challenge.goal;
        this.difficulty = Integer.toString(challenge.difficulty);
    }
}
