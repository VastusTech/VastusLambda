package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract public class Event extends DatabaseObject {
    public String owner;
    public TimeInterval time;
    public Set<String> members;
    public int capacity;
    public String access;

    public Event(Item item) throws Exception {
        super(item);
        this.owner = item.getString("owner");
        this.time = new TimeInterval(item.getString("time"));
        this.members = item.getStringSet("members");
        if (this.members == null) { this.members = new HashSet<>(); }
        this.capacity = Integer.parseInt(item.getString("capacity"));
        this.access = item.getString("access");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("capacity", new AttributeValue("4"));
        item.put("access", new AttributeValue("public"));
        return item;
    }
}
