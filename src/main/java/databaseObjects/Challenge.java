package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public String group;
    public String goal;
    public int difficulty;
    public String winner;
    public String prize;
    public Set<String> tags;
    public Set<String> submissions;

    public Challenge(Item item) throws Exception {
        super(item);
        this.owner = item.getString("owner");
        this.endTime = new DateTime(item.getString("endTime"));
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
        this.ifCompleted = Boolean.parseBoolean(item.getString("ifCompleted"));
        this.events = item.getStringSet("events");
        if (this.events == null) { this.events = new HashSet<>(); }
        this.group = item.getString("group");
        this.goal = item.getString("goal");
        String difficulty = item.getString("difficulty");
        if (difficulty != null) { this.difficulty = Integer.parseInt(difficulty); }
        else { this.difficulty = 0; }
        this.tags = item.getStringSet("tags");
        if (this.tags == null) { this.tags = new HashSet<>(); }
        this.winner = item.getString("winner");
        this.prize = item.getString("prize");
        this.submissions = item.getStringSet("submissions");
        if (this.submissions == null) { this.submissions = new HashSet<>(); }
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Challenge"));
        item.put("capacity", new AttributeValue("10"));
        item.put("access", new AttributeValue("public"));
        item.put("difficulty", new AttributeValue("1"));
        item.put("ifCompleted", new AttributeValue("false"));
        return item;
    }

    public static Challenge readChallenge(String id) throws Exception {
        return (Challenge) read(tableName, getPrimaryKey("Challenge", id));
    }
}
