package main.java.lambdaFunctionHandlers.requestObjects;

import main.java.databaseOperations.exceptions.ExceedsDatabaseLimitException;

/**
 * The POJO for the request if the Lambda caller wants to create a Challenge in the database.
 */
public class CreateChallengeRequest extends CreateObjectRequest {
    // Required
    public String owner;
    public String endTime;
    public String capacity;
    public String title;
    public String goal;

    // Optional
    public String group;
    public String description;
    public String[] tags;
    public String[] members;
    public String access;
    public String restriction;
    public String difficulty;
    public String prize;
    public String challengeType;
    public String streakUpdateSpanType;
    public String streakUpdateInterval;
    public String streakN;

    public CreateChallengeRequest(String owner, String endTime, String capacity, String title, String goal, String
            challengeType, String group, String description, String[] tags, String[] members, String access, String
            restriction, String difficulty, String prize, String streakUpdateSpanType, String streakUpdateInterval,
                                  String streakN) {
        this.owner = owner;
        this.endTime = endTime;
        this.capacity = capacity;
        this.title = title;
        this.goal = goal;
        this.challengeType = challengeType;
        this.group = group;
        this.description = description;
        this.tags = tags;
        this.members = members;
        this.access = access;
        this.restriction = restriction;
        this.difficulty = difficulty;
        this.prize = prize;
        this.streakUpdateSpanType = streakUpdateSpanType;
        this.streakUpdateInterval = streakUpdateInterval;
        this.streakN = streakN;
    }

    public CreateChallengeRequest() {}

    @Override
    public boolean ifHasEmptyString() throws ExceedsDatabaseLimitException {
        return hasEmptyString(owner, endTime, capacity, title, goal, challengeType, group,
                description, access, restriction, difficulty, prize, streakUpdateSpanType,
                streakUpdateInterval, streakN)
                || arrayHasEmptyString(tags, members);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
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

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getStreakUpdateSpanType() {
        return streakUpdateSpanType;
    }

    public void setStreakUpdateSpanType(String streakUpdateSpanType) {
        this.streakUpdateSpanType = streakUpdateSpanType;
    }

    public String getStreakUpdateInterval() {
        return streakUpdateInterval;
    }

    public void setStreakUpdateInterval(String streakUpdateInterval) {
        this.streakUpdateInterval = streakUpdateInterval;
    }

    public String getStreakN() {
        return streakN;
    }

    public void setStreakN(String streakN) {
        this.streakN = streakN;
    }
}
