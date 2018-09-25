package main.java.lambdaFunctionHandlers.reviewFunctionHandlers;

import main.java.databaseObjects.Review;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadReviewsByID {
    public static List<ObjectResponse> handle(String[] reviewIDs) throws Exception {
        List<ObjectResponse> reviewResponses = new ArrayList<>();
        for (String id : reviewIDs) {
            reviewResponses.add(new ReviewResponse(Review.readReview(id)));
        }
        return reviewResponses;
    }
}
