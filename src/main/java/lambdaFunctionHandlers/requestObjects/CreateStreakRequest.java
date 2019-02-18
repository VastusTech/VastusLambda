package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateStreakRequest extends CreateObjectRequest {
    // Required
    public String owner;
    public String about;
    public String streakType;

    // Not Required
    public String updateType;

    public CreateStreakRequest(String owner, String about, String streakType, String updateType) {
        this.owner = owner;
        this.about = about;
        this.streakType = streakType;
        this.updateType = updateType;
    }

    public CreateStreakRequest() {}

    @Override
    public boolean ifHasEmptyString() {
        return hasEmptyString(owner, about, streakType, updateType);
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

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }
}
