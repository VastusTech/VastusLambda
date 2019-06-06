package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import org.junit.Test;

import main.java.databaseObjects.Review;
import main.java.logic.TimeHelper;

// TODO REVISIT ONCE IMPLEMENTED BETTER
public class ReviewTest {
    static Item getReviewTestItem() {
        return new Item()
                .withString("item_type", "Review")
                .withString("id", "RE0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("by", "BY")
                .withString("about", "ABOUT")
                .withString("friendlinessRating", "5.0")
                .withString("effectivenessRating", "5.0")
                .withString("reliabilityRating", "5.0")
                .withString("description", "DESCRIPTION");

    }

    @Test
    public void test() {
        Review review;
    }
}
