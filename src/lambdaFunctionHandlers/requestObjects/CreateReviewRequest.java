package lambdaFunctionHandlers.requestObjects;

public class CreateReviewRequest {
    public String byID;
    public String aboutID;
    public String friendlinessRating;
    public String effectivenessRating;
    public String reliabilityRating;
    public String description;

    public CreateReviewRequest(String byID, String aboutID, String friendlinessRating, String effectivenessRating, String reliabilityRating, String description) {
        this.byID = byID;
        this.aboutID = aboutID;
        this.friendlinessRating = friendlinessRating;
        this.effectivenessRating = effectivenessRating;
        this.reliabilityRating = reliabilityRating;
        this.description = description;
    }

    public CreateReviewRequest() {}

    public String getByID() {

        return byID;
    }

    public void setByID(String byID) {
        this.byID = byID;
    }

    public String getAboutID() {
        return aboutID;
    }

    public void setAboutID(String aboutID) {
        this.aboutID = aboutID;
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
