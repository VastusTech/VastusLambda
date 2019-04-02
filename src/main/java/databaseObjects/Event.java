package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    public Event(Item item) throws Exception {
        super(item);
        this.owner = item.getString("owner");
        this.time = new TimeInterval(item.getString("time"));
        this.members = item.getStringSet("members");
        if (this.members == null) { this.members = new HashSet<>(); }
        this.invitedMembers = item.getStringSet("invitedMembers");
        if (this.invitedMembers == null) { this.invitedMembers = new HashSet<>(); }
        this.memberRequests = item.getStringSet("memberRequests");
        if (this.memberRequests == null) { this.memberRequests = new HashSet<>(); }
        this.receivedInvites = item.getStringSet("receivedInvites");
        if (this.receivedInvites == null) { this.receivedInvites = new HashSet<>(); }
        this.capacity = Integer.parseInt(item.getString("capacity"));
        this.access = item.getString("access");
        this.restriction = item.getString("restriction");
        this.title = item.getString("title");
        this.description = item.getString("description");
        this.address = item.getString("address");
        this.challenge = item.getString("challenge");
        this.group = item.getString("group");
        this.ifCompleted = Boolean.parseBoolean(item.getString("ifCompleted"));
        this.tags = item.getStringSet("tags");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Event"));
        item.put("capacity", new AttributeValue("10"));
        item.put("access", new AttributeValue("private"));
        item.put("ifCompleted", new AttributeValue("false"));
        return item;
    }

    public static Event readEvent(String id) throws Exception {
        return (Event) read(tableName, getPrimaryKey("Event", id));
    }
}
