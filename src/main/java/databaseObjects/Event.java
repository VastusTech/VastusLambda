package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.EventResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Event extends DatabaseObject {
    public String title;
    public String description;
    public String address;
    public String owner;
    public TimeInterval time;
    public Set<String> members;
    public int capacity;
    public String access;
    public boolean ifChallenge;

    public String goal;
    public int difficulty;
    public String winner;

    public Event(Item item) throws Exception {
        super(item);
        this.owner = item.getString("owner");
        this.time = new TimeInterval(item.getString("time"));
        this.members = item.getStringSet("members");
        if (this.members == null) { this.members = new HashSet<>(); }
        this.capacity = Integer.parseInt(item.getString("capacity"));
        this.access = item.getString("access");
        this.title = item.getString("title");
        this.description = item.getString("description");
        this.address = item.getString("address");
        this.ifChallenge = Boolean.parseBoolean(item.getString("ifChallenge"));
        this.goal = item.getString("goal");
        this.difficulty = Integer.parseInt(item.getString("difficulty"));
        this.winner = item.getString("winner");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Challenge"));
        item.put("capacity", new AttributeValue("4"));
        item.put("access", new AttributeValue("public"));
        item.put("title", new AttributeValue("untitled"));
        item.put("description", new AttributeValue("Put your description here."));
        item.put("goal", new AttributeValue("Put your goal here."));
        item.put("difficulty", new AttributeValue("3"));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new EventResponse(this);
    }

    public static Event readEvent(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));
        key.put("item_type", new AttributeValue("Event"));
        return DynamoDBHandler.getInstance().readItem(key);
    }
}
