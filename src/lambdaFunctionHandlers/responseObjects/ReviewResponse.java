package lambdaFunctionHandlers.responseObjects;

import databaseObjects.Review;

public class ReviewResponse extends ObjectResponse {
    private String itemType = "Review";
    private String byID;
    private String aboutID;
    private String friendlinessRating;
    private String effectivenessRating;
    private String reliabilityRating;
    private String description;

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
