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

//    public Map<String, AttributeValue> item;
//    public Map<String, AttributeValue> key;
//    public Map<String, AttributeValue> attributes;

//    public DatabaseObject(String id, String itemType, String marker, String timeCreated, Map<String, AttributeValue>
//            attributes) {
//        this.id = id;
//        this.itemType = itemType;
//        this.marker = marker;
//        this.timeCreated = timeCreated;
//
//        key = new HashMap<>();
//        key.put("id", new AttributeValue(id));
//        key.put("item_type", new AttributeValue(itemType));
//
//        this.attributes = new HashMap<>();
//        this.attributes.putAll(attributes);
//
//        item = new HashMap<>();
//        item.putAll(key);
//        item.putAll(attributes);
//    }

    public DatabaseObject(Item item) throws Exception {
        this.id = item.getString("id");
        this.itemType = item.getString("item_type");
        // TODO Surely there must be a better way to do this? Test if you just getString?
        this.marker = Integer.toString(item.getNumber("marker").intValueExact());
        this.timeCreated = item.getString("timeCreated");

//        this.item = new HashMap<>();
//        this.key = new HashMap<>();
//        this.attributes = new HashMap<>();

        // https://stackoverflow.com/a/9009709
//        for(Map.Entry<String, Object> entry : item.asMap().entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            // AttributeValue value = entry.getValue();
//            if (key.equals("id") || key.equals("item_type")) {
//                this.key.put(key, value);
//            }
//            else {
//                this.attributes.put(key, value);
//            }
//        }
    }

//    public void putAttribute(String key, AttributeValue value) {
//        // You have to also put it into the item
//        attributes.put(key, value);
//        item.put(key, value);
//    }

    abstract public ObjectResponse getResponse();

    static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = new HashMap<>();
        // item.put("id", new AttributeValue("null"));
        item.put("id", null);
        // item.put("item_type", new AttributeValue("null"));
        item.put("item_type", null);
        // item.put("time_created", new AttributeValue("null"));
        item.put("timeCreated", null);
        item.put("marker", new AttributeValue().withN("0"));
        return item;
    }
}
