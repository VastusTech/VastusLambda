package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
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
    public String trainer;
    public Set<String> clients;
    public int capacity;
    public String gym;
    public String sticker;
    public String intensity;
    public Set<String> missingReviews;
    public int price;

    Workout(Item item) throws Exception {
        super(item);
        this.time = new TimeInterval(item.getString("time"));
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

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Workout"));
        // item.put("time", new AttributeValue(Constants.nullAttributeValue));
        // item.put("time", null);
        // item.put("trainerID", new AttributeValue(Constants.nullAttributeValue));
        // item.put("trainerID", null);
        // item.put("clientIDs", new AttributeValue(new ArrayList<>()));
        // item.put("clientIDs", null);
        // item.put("capacity", new AttributeValue(Constants.nullAttributeValue));
        item.put("capacity", new AttributeValue("4"));
        // item.put("gymID", new AttributeValue(Constants.nullAttributeValue));
        // item.put("gymID", null);
        // item.put("sticker", new AttributeValue(Constants.nullAttributeValue));
        // item.put("sticker", null);
        // item.put("intensity", new AttributeValue(Constants.nullAttributeValue));
        // item.put("intensity", null);
//        item.put("missing_reviews", new AttributeValue(new ArrayList<>()));
        // item.put("missing_reviews", null);
        // item.put("price", new AttributeValue(Constants.nullAttributeValue));
        item.put("price", new AttributeValue("80"));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new WorkoutResponse(this);
    }

    // TODO Implement cache system here again?
    public static Workout readWorkout(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Workout"));
        key.put("id", new AttributeValue(id));
        return DynamoDBHandler.getInstance().readItem(key);
    }
}
