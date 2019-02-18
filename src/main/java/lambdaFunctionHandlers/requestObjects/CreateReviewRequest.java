package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateReviewRequest extends CreateObjectRequest {
    // Required
    public String by;
    public String about;
    public String friendlinessRating;
    public String effectivenessRating;
    public String reliabilityRating;
    public String description;

    public CreateReviewRequest(String by, String about, String friendlinessRating, String effectivenessRating, String reliabilityRating, String description) {
        this.by = by;
        this.about = about;
        this.friendlinessRating = friendlinessRating;
        this.effectivenessRating = effectivenessRating;
        this.reliabilityRating = reliabilityRating;
        this.description = description;
    }

    public CreateReviewRequest() {}

    @Override
    public boolean ifHasEmptyString() {
        return hasEmptyString(by, about, friendlinessRating, effectivenessRating, reliabilityRating, description);
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFriendlinessRating() {
        return friendlinessRating;
    }

    public void setFriendlinessRating(String friendlinessRating) {
        this.friendlinessRating = friendlinessRating;
    }

    public String getEffectivenessRating() {
        return effectivenessRating;
    }

    public void setEffectivenessRating(String effectivenessRating) {
        this.effectivenessRating = effectivenessRating;
    }

    public String getReliabilityRating() {
        return reliabilityRating;
    }

    public void setReliabilityRating(String reliabilityRating) {
        this.reliabilityRating = reliabilityRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
