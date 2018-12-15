package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Event extends DatabaseObject {
    public String owner;
    public String title;
    public String description;
    public String address;
    public TimeInterval time;
    public Set<String> members;
    public Set<String> invitedMembers;
    public Set<String> memberRequests;
    public int capacity;
    public String access;
    public String restriction;
    public boolean ifCompleted;
    public String challenge;
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
        this.capacity = Integer.parseInt(item.getString("capacity"));
        this.access = item.getString("access");
        this.restriction = item.getString("restriction");
        this.title = item.getString("title");
        this.description = item.getString("description");
        this.address = item.getString("address");
        this.challenge = item.getString("challenge");
        this.ifCompleted = Boolean.parseBoolean(item.getString("ifCompleted"));
        this.tags = item.getStringSet("tags");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Event"));
        item.put("capacity", new AttributeValue("10"));
        item.put("access", new AttributeValue("private"));
//        item.put("title", new AttributeValue("Untitled"));
//        item.put("description", new AttributeValue("Put your description here."));
        item.put("ifCompleted", new AttributeValue("false"));
        return item;
    }

    public static Event readEvent(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));
        key.put("item_type", new AttributeValue("Event"));
        return DynamoDBHandler.getInstance().readItem(key);
    }
}
