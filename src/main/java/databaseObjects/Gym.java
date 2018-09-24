package main.java.databaseObjects;

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

    Gym(Map<String, AttributeValue> item) throws Exception {
        super(item);
        this.address = item.get("address").getS();
        this.trainerIDs = new HashSet<>(item.get("trainerIDs").getSS());
        this.weeklyHours = TimeInterval.getTimeIntervals(item.get("weekly_hours").getSS());
        this.vacationTimes = TimeInterval.getTimeIntervals(item.get("vacation_times").getSS());
        this.sessionCapacity = Integer.parseInt(item.get("session_capacity").getS());
        this.gymType = item.get("gym_type").getS();
        this.paymentSplit = Float.parseFloat(item.get("payment_split").getS());
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("address", new AttributeValue(Constants.nullAttributeValue));
        item.put("trainerIDs", new AttributeValue(new ArrayList<>()));
        item.put("weekly_hours", new AttributeValue(new ArrayList<>()));
        item.put("vacation_times", new AttributeValue(new ArrayList<>()));
        item.put("session_capacity", new AttributeValue(Constants.nullAttributeValue));
        item.put("gym_type", new AttributeValue("independent"));
        item.put("payment_split", new AttributeValue(Constants.nullAttributeValue));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new GymResponse(this);
    }

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
