package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;

import java.util.Map;

import main.java.logic.TimeHelper;

/**
 * A Streak is a specific recurring numerical event keeper. This keeps track of how large someone's
 * streak is by updating it at a specific span of a specific interval, having to complete a task
 * streakN times before it is counted toward the N of the streak, or the main number.
 */
public class Streak extends DatabaseObject {
    public String owner;
    public String about;
    public int N;
    public int bestN;
    public int currentN;
    public DateTime lastUpdated;
    public DateTime lastAttemptStarted;
    public String streakType;
    public UpdateSpanType updateSpanType; // When it updates
    public int updateInterval; // After how many spans, it updates
    public int streakN; // The number of times to do tasks to maintain the streak

    /**
     * The main constructor for the Streak class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    Streak(Item item) throws Exception {
        super(item);
        this.owner = item.getString("owner");
        this.about = item.getString("about");
        this.N = item.getNumber("N").intValueExact();
        this.bestN = item.getNumber("bestN").intValueExact();
        this.currentN = item.getNumber("currentN").intValueExact();
        this.lastUpdated = new DateTime(item.getString("lastUpdated"));
        this.lastAttemptStarted = new DateTime(item.getString("lastAttemptStarted"));
        this.streakType = item.getString("streakType");
        this.updateSpanType = UpdateSpanType.valueOf(item.getString("updateSpanType"));
        this.updateInterval = Integer.parseInt(item.getString("updateInterval"));
        this.streakN = Integer.parseInt(item.getString("streakN"));
    }

    /**
     * Gets the empty item with the default values for the Streak object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Streak"));
        item.put("N", new AttributeValue().withN("0"));
        item.put("bestN", new AttributeValue().withN("0"));
        item.put("currentN", new AttributeValue().withN("0"));
        item.put("lastUpdated", new AttributeValue(TimeHelper.nowString()));
        item.put("lastAttemptStarted", new AttributeValue(TimeHelper.nowString()));
        return item;
    }

    /**
     * Reads a Streak from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Streak object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Streak readStreak(String id) throws Exception {
        return (Streak) read(tableName, getPrimaryKey("Streak", id));
    }

    /**
     * The values that indicate what kind of a Streak a specific Streak object is.
     */
    public enum StreakType {
        submission
    }

    /**
     * The values that indicate the potential updating span types for a Streak.
     */
    public enum UpdateSpanType {
        hourly,
        daily,
        weekly,
        monthly,
        yearly
    }
}
