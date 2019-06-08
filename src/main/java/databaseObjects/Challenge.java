package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import main.java.databaseOperations.exceptions.CorruptedItemException;

/**
 * This Challenge object represents an ongoing challenge that takes in submissions in order to rank
 * the competitors. Challenges have an end date and a winner is declared at the end of it by the
 * owner.
 *
 * Streak challenges are pretty much based on who has the highest {@link Streak} at the end of the
 * challenge.
 */
public class Challenge extends DatabaseObject {
    public String title;
    public String description;
    public String owner;
    public DateTime endTime;
    public Set<String> members;
    public Set<String> invitedMembers;
    public Set<String> memberRequests;
    public Set<String> receivedInvites;
    public int capacity;
    public boolean ifCompleted;
    public String access;
    public String restriction;
    public Set<String> events;
    public Set<String> completedEvents;
    public String group;
    public String goal;
    public String challengeType;
    public int difficulty;
    public String winner;
    public String prize;
    public Set<String> tags;
    public Set<String> submissions;
    public Set<String> streaks;
    public String streakUpdateSpanType;
    public String streakUpdateInterval;
    public String streakN;

    /**
     * The main constructor for the Challenge class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Challenge(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Challenge")) throw new CorruptedItemException("Challenge initialized for wrong item type");
        this.owner = item.getString("owner");
        if (owner == null) { throw new CorruptedItemException("Challenge needs an owner"); }
        if (item.getString("endTime") == null) throw new CorruptedItemException("End Time cannot be null");
        this.endTime = new DateTime(item.getString("endTime"));
        this.members = item.getStringSet("members");
        if (this.members == null) { this.members = new HashSet<>(); }
        this.invitedMembers = item.getStringSet("invitedMembers");
        if (this.invitedMembers == null) { this.invitedMembers = new HashSet<>(); }
        this.memberRequests = item.getStringSet("memberRequests");
        if (this.memberRequests == null) { this.memberRequests = new HashSet<>(); }
        this.receivedInvites = item.getStringSet("receivedInvites");
        if (this.receivedInvites == null) { this.receivedInvites = new HashSet<>(); }
        try { this.capacity = Integer.parseInt(item.getString("capacity")); }
        catch (NumberFormatException e) {
            throw new CorruptedItemException("Capacity is malformed or null!", e);
        }
        this.access = item.getString("access");
        if (access == null) { throw new CorruptedItemException("Challenge access may not be null"); }
        this.restriction = item.getString("restriction");
        this.title = item.getString("title");
        if (title == null) { throw new CorruptedItemException("Challenge title may not be null"); }
        this.description = item.getString("description");
        this.ifCompleted = Boolean.parseBoolean(item.getString("ifCompleted"));
        this.events = item.getStringSet("events");
        if (this.events == null) { this.events = new HashSet<>(); }
        this.completedEvents = item.getStringSet("completedEvents");
        if (this.completedEvents == null) { this.completedEvents = new HashSet<>(); }
        this.group = item.getString("group");
        this.goal = item.getString("goal");
        if (goal == null) throw new CorruptedItemException("Goal cannot be null");
        this.challengeType = item.getString("challengeType");
        String difficulty = item.getString("difficulty");
        if (difficulty != null) { this.difficulty = Integer.parseInt(difficulty); }
        else { this.difficulty = 0; }
        this.tags = item.getStringSet("tags");
        if (this.tags == null) { this.tags = new HashSet<>(); }
        this.winner = item.getString("winner");
        this.prize = item.getString("prize");
        this.submissions = item.getStringSet("submissions");
        if (this.submissions == null) { this.submissions = new HashSet<>(); }
        this.streaks = item.getStringSet("streaks");
        if (this.streaks == null) { this.streaks = new HashSet<>(); }
        this.streakUpdateSpanType = item.getString("streakUpdateSpanType");
        this.streakUpdateInterval = item.getString("streakUpdateInterval");
        this.streakN = item.getString("streakN");
    }

    /**
     * Gets the empty item with the default values for the Challenge object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Challenge"));
        item.put("capacity", new AttributeValue("10"));
//        item.put("access", new AttributeValue("public"));
        item.put("difficulty", new AttributeValue("0"));
        item.put("ifCompleted", new AttributeValue("false"));
        return item;
    }

    /**
     * Reads a Challenge from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Challenge object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Challenge readChallenge(String id) throws Exception {
        return (Challenge) read(getTableName(), getPrimaryKey("Challenge", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Challenge) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Challenge)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(members, invitedMembers, memberRequests, receivedInvites, events,
                completedEvents, tags, submissions, streaks));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, owner, endTime, capacity,
                ifCompleted, access, restriction, group, goal, challengeType, difficulty, winner,
                prize, streakUpdateSpanType, streakUpdateInterval, streakN);
    }
}
