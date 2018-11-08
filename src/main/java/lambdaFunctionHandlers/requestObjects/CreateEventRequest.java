package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateEventRequest {
    // Required
    public String owner;
    public String time;
    public String capacity;
    public String address;
    public String title;
    public String ifChallenge;

    // Required if ifChallenge
    public String goal;

    // Optional
    public String description;
    public String difficulty;
    public String[] members;
    public String access;

    public CreateEventRequest(String owner, String time, String capacity, String address, String title, String goal,
        String ifChallenge, String description, String difficulty, String[] members, String access) {
        this.owner = owner;
        this.time = time;
        this.capacity = capacity;
        this.address = address;
        this.title = title;
        this.goal = goal;
        this.ifChallenge = ifChallenge;
        this.description = description;
        this.difficulty = difficulty;
        this.members = members;
        this.access = access;
    }

    public CreateEventRequest() {}

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getIfChallenge() {
        return ifChallenge;
    }

    public void setIfChallenge(String ifChallenge) {
        this.ifChallenge = ifChallenge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}

