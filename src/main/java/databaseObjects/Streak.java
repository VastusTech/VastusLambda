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

    // TODO Implement cache system here again?
    public static Streak readStreak(String id) throws Exception {
        return (Streak) read(tableName, getPrimaryKey("Streak", id));
    }

    public enum StreakType {
        submission
    }

    public enum UpdateSpanType {
        hourly,
        daily,
        weekly,
        monthly,
        yearly
    }
}
