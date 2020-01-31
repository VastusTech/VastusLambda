package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.exceptions.CorruptedItemException;

import java.util.*;

/**
 * Gyms are essentially employers that house a group of Trainers all together in order to allow them
 * to house Vastus personal training on their premises. They have a location, weekly hours, and a
 * session capacity.
 */
public class Gym extends User {
    public String address;
    public Set<String> trainers;
    public List<TimeInterval> weeklyHours;
    public List<TimeInterval> vacationTimes;
    public int sessionCapacity;
    public String gymType;
    public float paymentSplit;

    /**
     * The main constructor for the Gym class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Gym(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Gym")) throw new CorruptedItemException("Gym initialized for wrong item type");
        this.address = item.getString("address");
        this.trainers = item.getStringSet("trainers");
        if (trainers == null) { this.trainers = new HashSet<>(); }
        Set<String> weeklyHours = item.getStringSet("weeklyHours");
        if (weeklyHours != null) { this.weeklyHours = TimeInterval.getTimeIntervals(weeklyHours); }
        else { this.weeklyHours = new ArrayList<>(); }
        Set<String> vacationTimes = item.getStringSet("vacationTimes");
        if (vacationTimes != null) { this.vacationTimes = TimeInterval.getTimeIntervals(vacationTimes); }
        else { this.vacationTimes = new ArrayList<>(); }
        String sessionCapacity = item.getString("sessionCapacity");
        if (sessionCapacity != null) { this.sessionCapacity = Integer.parseInt(sessionCapacity); }
        else { this.sessionCapacity = -1; }
        this.gymType = item.getString("gymType");
        String paymentSplit = item.getString("paymentSplit");
        if (paymentSplit != null) { this.paymentSplit = Float.parseFloat(paymentSplit); }
        else { this.paymentSplit = -1; }
    }

    /**
     * Gets the empty item with the default values for the Gym object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Gym"));
        // item.put("address", new AttributeValue(Constants.nullAttributeValue));
        // item.put("trainerIDs", null);
        // item.put("weekly_hours", null);
        // item.put("vacation_times", null);
        item.put("sessionCapacity", new AttributeValue("10"));
        item.put("gymType", new AttributeValue("independent"));
        item.put("paymentSplit", new AttributeValue("50"));
        return item;
    }

    /**
     * Reads a Gym from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Gym object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Gym readGym(String id) throws Exception {
        return (Gym) read(getTableName(), getPrimaryKey("Gym", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Gym) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Gym)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(trainers, weeklyHours, vacationTimes));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address, sessionCapacity, gymType, paymentSplit);
    }
}
