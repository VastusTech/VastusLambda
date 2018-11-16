package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import main.java.Logic.Constants;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.TrainerResponse;

import java.util.*;

public class Trainer extends User {
    public String gym;
    public List<TimeInterval> availableTimes;
    public String workoutSticker;
    public String preferredIntensity;
    public int workoutCapacity;
    public int workoutPrice;
    public Set<String> followers;

    Trainer(Item item) throws Exception {
        super(item);
        this.gym = item.getString("gym");
        Set<String> availableTimes = item.getStringSet("availableTimes");
        if (availableTimes != null) { this.availableTimes = TimeInterval.getTimeIntervals(availableTimes); }
        else { this.availableTimes = new ArrayList<>(); }
        this.workoutSticker = item.getString("workoutSticker");
        this.preferredIntensity = item.getString("preferredIntensity");
        this.workoutCapacity = Integer.parseInt(item.getString("workoutCapacity"));
        this.workoutPrice = Integer.parseInt(item.getString("workoutPrice"));
        this.followers = item.getStringSet("followers");
        if (followers == null) { this.followers = new HashSet<>(); }
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
        item.put("workoutCapacity", new AttributeValue("4"));
        item.put("workoutPrice", new AttributeValue("80"));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new TrainerResponse(this);
    }

    // TODO Implement cache system here again?
    public static Trainer readTrainer(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Trainer"));
        key.put("id", new AttributeValue(id));
        return DynamoDBHandler.getInstance().readItem(key);
    }

    public static Trainer queryTrainer(String username) throws Exception {
        return DynamoDBHandler.getInstance().usernameQuery(username, "Trainer");
    }
}
