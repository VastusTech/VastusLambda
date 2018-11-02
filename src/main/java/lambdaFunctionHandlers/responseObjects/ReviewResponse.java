package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Review;

public class ReviewResponse extends ObjectResponse {
    public String item_type = "Review";
    public String by;
    public String about;
    public String friendlinessRating;
    public String effectivenessRating;
    public String reliabilityRating;
    public String description;

    public ReviewResponse(Review review) {
        super(review.id);
        this.by = review.by;
        this.about = review.about;
        this.friendlinessRating = Float.toString(review.friendlinessRating);
        this.effectivenessRating = Float.toString(review.effectivenessRating);
        this.reliabilityRating = Float.toString(review.reliabilityRating);
        this.description = review.description;
    }
}
