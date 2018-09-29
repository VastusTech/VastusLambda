package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.GymResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.*;

public class Gym extends User {
    public String address;
    public Set<String> trainerIDs;
    public List<TimeInterval> weeklyHours;
    public List<TimeInterval> vacationTimes;
    public int sessionCapacity;
    public String gymType;
    public float paymentSplit;

    Gym(Item item) throws Exception {
        super(item);
        this.address = item.getString("address");
        this.trainerIDs = item.getStringSet("trainerIDs");
        if (trainerIDs == null) { this.trainerIDs = new HashSet<>(); }
        Set<String> weeklyHours = item.getStringSet("weekly_hours");
        if (weeklyHours != null) { this.weeklyHours = TimeInterval.getTimeIntervals(weeklyHours); }
        else { this.weeklyHours = new ArrayList<>(); }
        Set<String> vacationTimes = item.getStringSet("vacation_times");
        if (vacationTimes != null) { this.vacationTimes = TimeInterval.getTimeIntervals(vacationTimes); }
        else { this.vacationTimes = new ArrayList<>(); }
        String sessionCapacity = item.getString("session_capacity");
        if (sessionCapacity != null) { this.sessionCapacity = Integer.parseInt(sessionCapacity); }
        this.gymType = item.getString("gym_type");
        String paymentSplit = item.getString("payment_split");
        if (paymentSplit != null) { this.paymentSplit = Float.parseFloat(paymentSplit); }
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Gym"));
        // item.put("address", new AttributeValue(Constants.nullAttributeValue));
        // item.put("trainerIDs", null);
        // item.put("weekly_hours", null);
        // item.put("vacation_times", null);
        // TODO Put a default session capacity and a default payment split
        item.put("session_capacity", new AttributeValue("10"));
        item.put("gym_type", new AttributeValue("independent"));
        item.put("payment_split", new AttributeValue("50"));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new GymResponse(this);
    }

    // TODO Implement cache system here again?
    public static Gym readGym(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Gym"));
        key.put("id", new AttributeValue(id));
        return DynamoDBHandler.getInstance().readItem(key);
    }

    public static Gym queryGym(String username) throws Exception {
        return DynamoDBHandler.getInstance().usernameQuery(username, "Gym");
    }
}
