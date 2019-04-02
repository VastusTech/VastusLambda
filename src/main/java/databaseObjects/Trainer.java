package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.*;

/**
 * A Trainer represents a User that is a fitness expert that is curating content on our platform for
 * others to utilize. They have a portal that Users can subscribe to and they can also will be able
 * to sell and schedule their personal training on the platform as well.
 */
public class Trainer extends User {
    public String gym;
    public List<TimeInterval> availableTimes;
    public String workoutSticker;
    public String preferredIntensity;
    public int workoutCapacity;
    public int workoutPrice;
    public Set<String> followers;
    public Set<String> subscribers;
    public String subscriptionPrice;
    public Set<String> certifications;

    Trainer(Item item) throws Exception {
        super(item);
        this.gym = item.getString("gym");
        Set<String> availableTimes = item.getStringSet("availableTimes");
        if (availableTimes != null) { this.availableTimes = TimeInterval.getTimeIntervals(availableTimes); }
        else { this.availableTimes = new ArrayList<>(); }
        this.workoutSticker = item.getString("workoutSticker");
        this.preferredIntensity = item.getString("preferredIntensity");
        String workoutCapacity = item.getString("workoutCapacity");
        if (workoutCapacity != null) { this.workoutCapacity = Integer.parseInt(workoutCapacity); }
        String workoutPrice = item.getString("workoutPrice");
        if (workoutPrice != null) { this.workoutPrice = Integer.parseInt(workoutPrice); }
        this.followers = item.getStringSet("followers");
        if (followers == null) { this.followers = new HashSet<>(); }
        this.subscribers = item.getStringSet("subscribers");
        if (subscribers == null) { this.subscribers = new HashSet<>(); }
        this.subscriptionPrice = item.getString("subscriptionPrice");
        this.certifications = item.getStringSet("certifications");
        if (certifications == null) { this.certifications = new HashSet<>(); }
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Trainer"));
        // item.put("gymID", new AttributeValue(Constants.nullAttributeValue));
        item.put("gym", null);
        item.put("availableTimes", null);
        // item.put("workout_sticker", new AttributeValue(Constants.nullAttributeValue));
        item.put("workoutSticker", null);
        // item.put("preferred_intensity", new AttributeValue(Constants.nullAttributeValue));
        item.put("preferredIntensity", null);
        item.put("workoutCapacity", null);
        item.put("workoutPrice", null);
        item.put("subscriptionPrice", null);
        return item;
    }

    // TODO Implement cache system here again?
    public static Trainer readTrainer(String id) throws Exception {
        return (Trainer) read(tableName, getPrimaryKey("Trainer", id));
    }

//    public static Trainer queryTrainer(String username) throws Exception {
//        return DynamoDBHandler.getInstance().usernameQuery(username, "Trainer");
//    }
}
