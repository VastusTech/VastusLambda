package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Challenge;

public class ChallengeResponse extends ObjectResponse {
    public String item_type = "Challenge";
    public String owner;
    public String time;
    public String[] members;
    public String capacity;
    public String access;
    public String address;

    public String title;
    public String description;
    public String goal;
    public String difficulty;
    public String winner;

    public ChallengeResponse(Challenge challenge) {
        super(challenge.id);
        this.owner = challenge.owner;
        this.time = challenge.time.toString();
        this.members = challenge.members.toArray(new String[]{});
        this.capacity = Integer.toString(challenge.capacity);
        this.access = challenge.access;
        this.address = challenge.address;
        this.title = challenge.title;
        this.description = challenge.description;
        this.goal = challenge.goal;
        this.difficulty = Integer.toString(challenge.difficulty);
        this.winner = challenge.winner;
    }
}
