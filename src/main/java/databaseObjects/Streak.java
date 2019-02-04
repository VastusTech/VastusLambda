package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;

import java.util.Map;

public class Streak extends DatabaseObject {
    public String owner;
    public String about;
    public int N;
    public int bestN;
    public int currentN;
    public DateTime lastUpdated;
    public String streakType;
    public String updateType;

    Streak(Item item) throws Exception {
        super(item);
        this.owner = item.getString("owner");
        this.about = item.getString("about");
        this.N = item.getNumber("N").intValueExact();
        this.bestN = item.getNumber("bestN").intValueExact();
        this.currentN = item.getNumber("currentN").intValueExact();
        this.lastUpdated = new DateTime(item.getString("lastUpdated"));
        this.streakType = item.getString("streakType");
        this.updateType = item.getString("updateType");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Streak"));
        item.put("N", new AttributeValue().withN("0"));
        item.put("bestN", new AttributeValue().withN("0"));
        item.put("currentN", new AttributeValue().withN("0"));
        item.put("lastUpdated", new AttributeValue(new DateTime().toString()));
        return item;
    }

    // TODO Implement cache system here again?
    public static Streak readStreak(String id) throws Exception {
        return (Streak) read(tableName, getPrimaryKey("Streak", id));
    }

    public enum StreakType {
        submission
    }
}
