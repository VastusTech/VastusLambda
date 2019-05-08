package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.*;

/**
 * A Workout represents a single/group personal training session with a single Trainer in a Gym.
 */
public class Workout extends DatabaseObject {
    public TimeInterval time;
    public boolean ifCompleted;
    public String trainer;
    public Set<String> clients;
    public int capacity;
    public String gym;
    public String sticker;
    public String intensity;
    public Set<String> missingReviews;
    public int price;

    /**
     * The main constructor for the Workout class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    Workout(Item item) throws Exception {
        super(item);
        this.time = new TimeInterval(item.getString("time"));
        this.ifCompleted = Boolean.parseBoolean(item.getString("ifCompleted"));
        this.trainer = item.getString("trainer");
        this.clients = item.getStringSet("clients");
        if (clients == null) { this.clients = new HashSet<>(); }
        this.capacity = Integer.parseInt(item.getString("capacity"));
        this.gym = item.getString("gym");
        this.sticker = item.getString("sticker");
        this.intensity = item.getString("intensity");
        this.missingReviews = item.getStringSet("missingReviews");
        if (missingReviews == null) { this.missingReviews = new HashSet<>(); }
        this.price = Integer.parseInt(item.getString("price"));
    }

    /**
     * Gets the empty item with the default values for the Workout object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Workout"));
        item.put("ifCompleted", new AttributeValue("false"));
        item.put("capacity", new AttributeValue("4"));
        item.put("price", new AttributeValue("80"));
        return item;
    }

    /**
     * Reads a Workout from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Workout object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Workout readWorkout(String id) throws Exception {
        return (Workout) read(getTableName(), getPrimaryKey("Workout", id));
    }
}
