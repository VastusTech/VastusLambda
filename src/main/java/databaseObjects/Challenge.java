package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.ChallengeResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.HashMap;
import java.util.Map;

public class Challenge extends Event {
    public String title;
    public String description;
    public String address;
    public String goal;
    public int difficulty;
    public String winner;

    public Challenge(Item item) throws Exception {
        super(item);
        this.title = item.getString("title");
        this.description = item.getString("description");
        this.address = item.getString("address");
        this.goal = item.getString("goal");
        this.difficulty = Integer.parseInt(item.getString("difficulty"));
        this.winner = item.getString("winner");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = Event.getEmptyItem();
        item.put("item_type", new AttributeValue("Challenge"));
        item.put("title", new AttributeValue("untitled"));
        item.put("description", new AttributeValue("Put your description here."));
        item.put("goal", new AttributeValue("Put your goal here."));
        item.put("difficulty", new AttributeValue("3"));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new ChallengeResponse(this);
    }

    public static Challenge readChallenge(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));
        key.put("item_type", new AttributeValue("Challenge"));
        return DynamoDBHandler.getInstance().readItem(key);
    }
}
