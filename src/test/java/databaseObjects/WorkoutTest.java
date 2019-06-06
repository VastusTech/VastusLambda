package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import org.joda.time.DateTime;
import org.mockito.internal.util.collections.Sets;

import main.java.databaseObjects.TimeInterval;
import main.java.logic.TimeHelper;

// TODO REVISIT ONCE IMPLEMENTED BETTER
public class WorkoutTest {
    static Item getWorkoutTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Workout")
                .withString("id", "WO0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("time", getTestTimeInterval().toString())
                .withString("ifCompleted", "false")
                .withString("trainer", "TRAINER")
                .withStringSet("clients", Sets.newSet("1", "2", "3"))
                .withString("capacity", "1000")
                .withString("gym", "GYM")
                .withString("sticker", "STICKER")
                .withString("intensity", "INTENSITY")
                .withStringSet("missingReviews", Sets.newSet("1", "2", "3"))
                .withString("price", "80");
    }

    private static TimeInterval getTestTimeInterval() throws Exception {
        return new TimeInterval(TimeHelper.isoString(new DateTime(2000, 10, 5, 12, 0))
                + "_" + TimeHelper.isoString(new DateTime(2000, 10, 5, 13, 0)));
    }
}
