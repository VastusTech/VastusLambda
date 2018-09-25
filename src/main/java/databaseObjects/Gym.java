package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import main.java.Logic.Constants;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.GymResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.TrainerResponse;

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
        Set<String> trainerIDs = item.getStringSet("trainerIDs");
        if (trainerIDs != null) { this.trainerIDs = trainerIDs; }
        else { this.trainerIDs = new HashSet<>(); }
        Set<String> weeklyHours = item.getStringSet("weekly_hours");
        if (weeklyHours != null) { this.weeklyHours = TimeInterval.getTimeIntervals(weeklyHours); }
        else { this.weeklyHours = new ArrayList<>(); }
        Set<String> vacationTimes = item.getStringSet("vacation_times");
        if (vacationTimes != null) { this.vacationTimes = TimeInterval.getTimeIntervals(vacationTimes); }
        else { this.vacationTimes = new ArrayList<>(); }
        this.sessionCapacity = Integer.parseInt(item.getString("session_capacity"));
        this.gymType = item.getString("gym_type");
        this.paymentSplit = Float.parseFloat(item.getString("payment_split"));
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Gym"));
        item.put("address", new AttributeValue(Constants.nullAttributeValue));
        item.put("trainerIDs", null);
        item.put("weekly_hours", null);
        item.put("vacation_times", null);
        item.put("session_capacity", new AttributeValue(Constants.nullAttributeValue));
        item.put("gym_type", new AttributeValue("independent"));
        item.put("payment_split", new AttributeValue(Constants.nullAttributeValue));
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
