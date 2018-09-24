package main.java.databaseObjects;

import main.java.Logic.Constants;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.GymResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.TrainerResponse;
import main.java.lambdaFunctionHandlers.responseObjects.WorkoutResponse;

import java.util.*;

public class Workout extends DatabaseObject {
    public TimeInterval time;
    public String trainerID;
    public Set<String> clientIDs;
    public int capacity;
    public String gymID;
    public String sticker;
    public String intensity;
    public Set<String> missingReviews;
    public int price;

    Workout(Map<String, AttributeValue> item) throws Exception {
        super(item);
        this.time = new TimeInterval(item.get("time").getS());
        this.trainerID = item.get("trainerID").getS();
        this.clientIDs = new HashSet<>(item.get("clientIDs").getSS());
        this.capacity = Integer.parseInt(item.get("capacity").getS());
        this.gymID = item.get("gymID").getS();
        this.sticker = item.get("sticker").getS();
        this.intensity = item.get("intensity").getS();
        this.missingReviews = new HashSet<>(item.get("missing_reviews").getSS());
        this.price = Integer.parseInt(item.get("price").getS());
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("time", new AttributeValue(Constants.nullAttributeValue));
        item.put("trainerID", new AttributeValue(Constants.nullAttributeValue));
        item.put("clientIDs", new AttributeValue(new ArrayList<>()));
        item.put("capacity", new AttributeValue(Constants.nullAttributeValue));
        item.put("gymID", new AttributeValue(Constants.nullAttributeValue));
        item.put("sticker", new AttributeValue(Constants.nullAttributeValue));
        item.put("intensity", new AttributeValue(Constants.nullAttributeValue));
        item.put("missing_reviews", new AttributeValue(new ArrayList<>()));
        item.put("price", new AttributeValue(Constants.nullAttributeValue));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new WorkoutResponse(this);
    }

    public static Workout readWorkout(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Workout"));
        key.put("id", new AttributeValue(id));
        return DynamoDBHandler.getInstance().readItem(key);
    }
}
