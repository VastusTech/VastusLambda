package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.PartyResponse;

import java.util.HashMap;
import java.util.Map;

public class Party extends Event {
    public String title;
    public String description;
    public String address;

    public Party(Item item) throws Exception {
        super(item);
        this.title = item.getString("title");
        this.description = item.getString("description");
        this.address = item.getString("address");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = Event.getEmptyItem();
        item.put("item_type", new AttributeValue("Party"));
        item.put("title", new AttributeValue("untitled"));
        item.put("description", new AttributeValue("Put your description here!"));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new PartyResponse(this);
    }

    public static Party readParty(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));
        key.put("item_type", new AttributeValue("Party"));
        return DynamoDBHandler.getInstance().readItem(key);
    }
}
