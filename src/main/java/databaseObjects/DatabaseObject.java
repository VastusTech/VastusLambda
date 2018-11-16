package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

abstract public class DatabaseObject {
    public String id;
    public String itemType;
    public String marker;
    public String timeCreated;

    public DatabaseObject(Item item) throws Exception {
        this.id = item.getString("id");
        this.itemType = item.getString("item_type");
        // TODO Surely there must be a better way to do this? Test if you just getString?
        this.marker = Integer.toString(item.getNumber("marker").intValueExact());
        this.timeCreated = item.getString("timeCreated");
    }

    abstract public ObjectResponse getResponse();

    static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", null);
        item.put("item_type", null);
        item.put("timeCreated", null);
        item.put("marker", new AttributeValue().withN("0"));
        return item;
    }
}
