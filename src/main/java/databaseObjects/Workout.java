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
    public String trainerID;
    public Set<String> clientIDs;
    public int capacity;
    public String gymID;
    public String sticker;
    public String intensity;
    public Set<String> missingReviews;
    public int price;

    Workout(Item item) throws Exception {
        super(item);
        this.time = new TimeInterval(item.getString("time"));
        this.trainerID = item.getString("trainerID");
        Set<String> clientIDs = item.getStringSet("clientIDs");
        if (clientIDs != null) { this.clientIDs = clientIDs; }
        else { this.clientIDs = new HashSet<>(); }
        this.capacity = Integer.parseInt(item.getString("capacity"));
        this.gymID = item.getString("gymID");
        this.sticker = item.getString("sticker");
        this.intensity = item.getString("intensity");
        Set<String> missingReviews = item.getStringSet("missing_reviews");
        if (missingReviews != null) { this.missingReviews = missingReviews; }
        else { this.missingReviews = new HashSet<>(); }
        this.price = Integer.parseInt(item.getString("price"));
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Workout"));
        // item.put("time", new AttributeValue(Constants.nullAttributeValue));
        item.put("time", null);
        // item.put("trainerID", new AttributeValue(Constants.nullAttributeValue));
        item.put("trainerID", null);
        // item.put("clientIDs", new AttributeValue(new ArrayList<>()));
        item.put("clientIDs", null);
        // item.put("capacity", new AttributeValue(Constants.nullAttributeValue));
        item.put("capacity", null);
        // item.put("gymID", new AttributeValue(Constants.nullAttributeValue));
        item.put("gymID", null);
        // item.put("sticker", new AttributeValue(Constants.nullAttributeValue));
        item.put("sticker", null);
        // item.put("intensity", new AttributeValue(Constants.nullAttributeValue));
        item.put("intensity", null);
//        item.put("missing_reviews", new AttributeValue(new ArrayList<>()));
        item.put("missing_reviews", null);
        // item.put("price", new AttributeValue(Constants.nullAttributeValue));
        item.put("price", null);
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
