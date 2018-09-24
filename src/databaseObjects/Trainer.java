package databaseObjects;

import Logic.Constants;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import databaseOperations.DynamoDBHandler;
import lambdaFunctionHandlers.responseObjects.ObjectResponse;
import lambdaFunctionHandlers.responseObjects.TrainerResponse;

import java.util.*;

public class Trainer extends User {
    public String gymID;
    public List<TimeInterval> availableTimes;
    public String workoutSticker;
    public String preferredIntensity;
    public int workoutCapacity;
    public int workoutPrice;

    Trainer(Map<String, AttributeValue> item) throws Exception {
        super(item);
        this.gymID = item.get("gymID").getS();
        this.availableTimes = TimeInterval.getTimeIntervals(item.get("available_times").getSS());
        this.workoutSticker = item.get("workout_sticker").getS();
        this.preferredIntensity = item.get("preferred_intensity").getS();
        this.workoutCapacity = Integer.parseInt(item.get("workout_capacity").getS());
        this.workoutPrice = Integer.parseInt(item.get("workout_price").getS());
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("gymID", new AttributeValue(Constants.nullAttributeValue));
        item.put("available_times", new AttributeValue(new ArrayList<>()));
        item.put("workout_sticker", new AttributeValue(Constants.nullAttributeValue));
        item.put("preferred_intensity", new AttributeValue(Constants.nullAttributeValue));
        item.put("workout_capacity", new AttributeValue("4"));
        item.put("workout_price", new AttributeValue("80"));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new TrainerResponse(this);
    }

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
