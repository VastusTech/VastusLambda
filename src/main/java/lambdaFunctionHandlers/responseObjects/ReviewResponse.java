package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Review;

public class ReviewResponse extends ObjectResponse {
    public String itemType = "Review";
    public String byID;
    public String aboutID;
    public String friendlinessRating;
    public String effectivenessRating;
    public String reliabilityRating;
    public String description;

    public ReviewResponse(Review review) {
        super(review.id);
        this.byID = review.byID;
        this.aboutID = review.aboutID;
        this.friendlinessRating = Float.toString(review.friendlinessRating);
        this.effectivenessRating = Float.toString(review.effectivenessRating);
        this.reliabilityRating = Float.toString(review.reliabilityRating);
        this.description = review.description;
    }
}
