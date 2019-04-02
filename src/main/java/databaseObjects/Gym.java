package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;

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

    Gym(Item item) throws Exception {
        super(item);
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
        this.gymType = item.getString("gymType");
        String paymentSplit = item.getString("paymentSplit");
        if (paymentSplit != null) { this.paymentSplit = Float.parseFloat(paymentSplit); }
    }

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

    // TODO Implement cache system here again?
    public static Gym readGym(String id) throws Exception {
        return (Gym) read(tableName, getPrimaryKey("Gym", id));
    }

//    public static Gym queryGym(String username) throws Exception {
//        return DynamoDBHandler.getInstance().usernameQuery(username, "Gym");
//    }
}
