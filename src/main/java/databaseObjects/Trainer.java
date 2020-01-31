package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.*;

import main.java.databaseOperations.exceptions.CorruptedItemException;

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

    /**
     * The main constructor for the Trainer class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Trainer(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Trainer")) throw new CorruptedItemException("Trainer initialized for wrong item type");
        this.gym = item.getString("gym");
        Set<String> availableTimes = item.getStringSet("availableTimes");
        if (availableTimes != null) { this.availableTimes = TimeInterval.getTimeIntervals(availableTimes); }
        else { this.availableTimes = new ArrayList<>(); }
        this.workoutSticker = item.getString("workoutSticker");
        this.preferredIntensity = item.getString("preferredIntensity");
        String workoutCapacity = item.getString("workoutCapacity");
        if (workoutCapacity != null) { this.workoutCapacity = Integer.parseInt(workoutCapacity); }
        else { this.workoutCapacity = -1; }
        String workoutPrice = item.getString("workoutPrice");
        if (workoutPrice != null) { this.workoutPrice = Integer.parseInt(workoutPrice); }
        else { this.workoutPrice = -1; }
        this.followers = item.getStringSet("followers");
        if (followers == null) { this.followers = new HashSet<>(); }
        this.subscribers = item.getStringSet("subscribers");
        if (subscribers == null) { this.subscribers = new HashSet<>(); }
        this.subscriptionPrice = item.getString("subscriptionPrice");
        this.certifications = item.getStringSet("certifications");
        if (certifications == null) { this.certifications = new HashSet<>(); }
    }

    /**
     * Gets the empty item with the default values for the Trainer object.
     *
     * @return The map of attribute values for the item.
     */
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

    /**
     * Reads a Trainer from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Trainer object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Trainer readTrainer(String id) throws Exception {
        return (Trainer) read(getTableName(), getPrimaryKey("Trainer", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Trainer) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Trainer)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(availableTimes, followers, subscribers, certifications));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gym, workoutSticker, preferredIntensity,
                workoutCapacity, workoutPrice, subscriptionPrice);
    }
}
