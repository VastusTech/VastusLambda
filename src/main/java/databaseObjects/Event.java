package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract public class Event extends DatabaseObject {
    public String ownerID;
    public TimeInterval time;
    public Set<String> memberIDs;
    public int capacity;
    public String access;

    public Event(Item item) throws Exception {
        super(item);
        this.ownerID = item.getString("ownerID");
        this.time = new TimeInterval(item.getString("time"));
        this.memberIDs = item.getStringSet("memberIDs");
        if (this.memberIDs == null) { this.memberIDs = new HashSet<>(); }
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
