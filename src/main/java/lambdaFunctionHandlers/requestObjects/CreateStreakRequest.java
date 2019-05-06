package main.java.lambdaFunctionHandlers.requestObjects;

/**
 * The POJO for the request if the Lambda caller wants to create a Streak in the database.
 */
public class CreateStreakRequest extends CreateObjectRequest {
    // Required
    public String owner;
    public String about;
    public String streakType;
    public String updateSpanType;
    public String updateInterval;
    public String streakN;

    // Not Required

    public CreateStreakRequest(String owner, String about, String streakType, String updateSpanType,
                               String updateInterval, String streakN) {
        this.owner = owner;
        this.about = about;
        this.streakType = streakType;
        this.updateSpanType = updateSpanType;
        this.updateInterval = updateInterval;
        this.streakN = streakN;
    }

    public CreateStreakRequest() {}

    @Override
    public boolean ifHasEmptyString() {
        return hasEmptyString(owner, about, streakType, updateInterval, streakN);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStreakType() {
        return streakType;
    }

    public void setStreakType(String streakType) {
        this.streakType = streakType;
    }

    public String getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(String updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getStreakN() {
        return streakN;
    }

    public void setStreakN(String streakN) {
        this.streakN = streakN;
    }
}
