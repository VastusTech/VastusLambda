package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.exceptions.CorruptedItemException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An Event is a specific thing at a place and a time. It is just a gathering of people somewhere to
 * do something. It can be a part of a Challenge, but it is largely separate from that. An owner has
 * control of the Event but TODO how do we get owners to "complete" an event?
 */
public class Event extends DatabaseObject {
    public String title;
    public String description;
    public String owner;
    public TimeInterval time;
    public String address;
    public Set<String> members;
    public Set<String> invitedMembers;
    public Set<String> memberRequests;
    public Set<String> receivedInvites;
    public int capacity;
    public boolean ifCompleted;
    public String access;
    public String restriction;
    public String challenge;
    public String group;
    public Set<String> tags;

    /**
     * The main constructor for the Event class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Event(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Event")) throw new CorruptedItemException("Event initialized for wrong item type");
        this.owner = item.getString("owner");
        if (owner == null) throw new CorruptedItemException("Owner may not be null");
        try { this.time = new TimeInterval(item.getString("time")); }
        catch (NullPointerException e) {
            throw new CorruptedItemException("Time cannot be null", e);
        }
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
            throw new CorruptedItemException("Capacity is malformed or null", e);
        }
        if (capacity <= 0) throw new CorruptedItemException("Capacity cannot be less than or equal to 0");
        this.access = item.getString("access");
        if (access == null) throw new CorruptedItemException("Access may not be null");
        this.restriction = item.getString("restriction");
        this.title = item.getString("title");
        if (title == null) throw new CorruptedItemException("Title may not be null");
        this.description = item.getString("description");
        this.address = item.getString("address");
        if (address == null) throw new CorruptedItemException("Address may not be null");
        this.challenge = item.getString("challenge");
        this.group = item.getString("group");
        if (item.getString("ifCompleted") == null) throw new CorruptedItemException("If Completed cannot be null");
        this.ifCompleted = Boolean.parseBoolean(item.getString("ifCompleted"));
        this.tags = item.getStringSet("tags");
        if (tags == null) { tags = new HashSet<>(); }
    }

    /**
     * Gets the empty item with the default values for the Event object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Event"));
//        item.put("capacity", new AttributeValue("10"));
//        item.put("access", new AttributeValue("private"));
        item.put("ifCompleted", new AttributeValue("false"));
        return item;
    }

    /**
     * Reads an Event from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Event object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Event readEvent(String id) throws Exception {
        return (Event) read(getTableName(), getPrimaryKey("Event", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Event) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Event)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(time, members, invitedMembers, memberRequests, receivedInvites,
                tags));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, owner, address, capacity,
                ifCompleted, access, restriction, challenge, group);
    }
}
