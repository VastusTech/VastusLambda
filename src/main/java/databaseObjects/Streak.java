package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import main.java.databaseOperations.exceptions.CorruptedItemException;
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
    public Streak(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Streak")) throw new CorruptedItemException("Streak initialized for wrong item type");
        this.owner = item.getString("owner");
        if (owner == null) throw new CorruptedItemException("Owner cannot be null");
        this.about = item.getString("about");
        if (about == null) throw new CorruptedItemException("About cannot be null");
        if (item.getNumber("N") == null) throw new CorruptedItemException("N cannot be null");
        this.N = item.getNumber("N").intValueExact();
        if (N < 0) throw new CorruptedItemException("N cannot be less than 0");
        if (item.getNumber("bestN") == null) throw new CorruptedItemException("Best N cannot be null");
        this.bestN = item.getNumber("bestN").intValueExact();
        if (bestN < 0) throw new CorruptedItemException("Best N cannot be less than 0");
        if (item.getNumber("currentN") == null) throw new CorruptedItemException("Current N cannot be null");
        this.currentN = item.getNumber("currentN").intValueExact();
        if (currentN < 0) throw new CorruptedItemException("Current N cannot be less than 0");
        if (item.getString("lastUpdated") == null) throw new CorruptedItemException("Last Updated cannot be null");
        this.lastUpdated = new DateTime(item.getString("lastUpdated"));
        if (item.getString("lastAttemptStarted") == null) throw new CorruptedItemException("Last Attempt Started cannot be null");
        this.lastAttemptStarted = new DateTime(item.getString("lastAttemptStarted"));
        this.streakType = item.getString("streakType");
        try { this.updateSpanType = UpdateSpanType.valueOf(item.getString("updateSpanType")); }
        catch (NullPointerException | IllegalArgumentException e) {
            throw new CorruptedItemException("Update Span Type invalid or null", e);
        }
        if (item.getString("updateInterval") == null) throw new CorruptedItemException("Streak N cannot be null");
        this.updateInterval = Integer.parseInt(item.getString("updateInterval"));
        if (updateInterval <= 0) throw new CorruptedItemException("Update Interval cannot be equal to or less than 0");
        if (item.getString("streakN") == null) throw new CorruptedItemException("Streak N cannot be null");
        this.streakN = Integer.parseInt(item.getString("streakN"));
        if (streakN <= 0) throw new CorruptedItemException("Streak N cannot be equal to or less than 0");
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
        return (Streak) read(getTableName(), getPrimaryKey("Streak", id));
    }

    // Streak logic Helper methods

    /**
     * If the Streak has been started yet and has been contributed to yet.
     *
     * @return Whether this Streak has been started yet.
     */
    public boolean hasStarted() {
        return currentN != 0;
    }

    public boolean hasExpired(DateTime now) throws Exception {
        return hasExpired(updateSpansPassed(now));
    }

    public boolean hasExpired(int updateSpansPassed) {
        // If the update spans have passed or the previous attempt had failed, then it is expired.
        return updateSpansPassed >= 2 * updateInterval
                || (updateSpansPassed >= updateInterval && currentN < streakN);
    }

    public boolean isInNewSpan(DateTime now) throws Exception {
        return isInNewSpan(updateSpansPassed(now));
    }

    public boolean isInNewSpan(int updateSpansPassed) {
        return updateSpansPassed >= updateInterval;
    }

    /**
     * Returns the number of full update spans (which define cycles of the Streak) between the
     * last attempt started and now.
     *
     * @param now The {@link DateTime} object which indicates what the method considers now.
     * @return The number of full update spans that have passed.
     * @throws Exception If the Streak's span type is unhandled.
     */
    public int updateSpansPassed(DateTime now) throws Exception {
        if (!hasStarted()) {
            // Freshly started so it is expired from the gecko
            return 2 * updateInterval;
        }
        switch (updateSpanType) {
            case hourly:
                return TimeHelper.hourStartsBetween(lastAttemptStarted, now);
            case daily:
                return TimeHelper.midnightsBetween(lastAttemptStarted, now);
            case weekly:
                return TimeHelper.mondaysBetween(lastAttemptStarted, now);
            case monthly:
                return TimeHelper.firstDatesOfMonthBetween(lastAttemptStarted, now);
            case yearly:
                return TimeHelper.firstDatesOfYearBetween(lastAttemptStarted, now);
            default:
                throw new Exception("Unhandled streak span type!");
        }
    }

    public DateTime getNextAttemptStarted() throws Exception {
        // We need to spoof the next attempt started so that it actually indicates the
        // beginning of the time interval that it is started in. This is for multiple
        // spanned Streaks and for multiple sub task Streaks. This way, someone cannot
        // complete the first task late and give themselves extra time.
        switch (updateSpanType) {
            case hourly:
                return TimeHelper.hoursAfter(lastAttemptStarted, updateInterval);
            case daily:
                return TimeHelper.daysAfter(lastAttemptStarted, updateInterval);
            case weekly:
                return TimeHelper.weeksAfter(lastAttemptStarted, updateInterval);
            case monthly:
                return TimeHelper.monthsAfter(lastAttemptStarted, updateInterval);
            case yearly:
                return TimeHelper.yearsAfter(lastAttemptStarted, updateInterval);
            default:
                throw new Exception("Unhandled streak span type!");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Streak) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Streak)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(lastUpdated, lastAttemptStarted));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), owner, about, N, bestN, currentN, streakType, updateSpanType, updateInterval, streakN);
    }

    /**
     * The values that indicate at what state the Streak object is currently at. Depends on the
     * current time and when the User updated it.
     */
    public enum StreakState {
        notStarted,
        completed,
        stillCompleting,
        notCompleted,
        expired,
    }

    /**
     * The values that indicate what kind of a Streak a specific Streak object is.
     */
    public enum StreakType {
        submission,
        coinage,
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
