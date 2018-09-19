package databaseObjects;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

abstract public class DatabaseObject {
    public String id;
    public String itemType;
    public String marker;
    public String timeCreated;

    public Map<String, AttributeValue> item;
    public Map<String, AttributeValue> key;
    public Map<String, AttributeValue> attributes;

    public DatabaseObject(String id, String itemType, String marker, String timeCreated, Map<String, AttributeValue>
            attributes) {
        this.id = id;
        this.itemType = itemType;
        this.marker = marker;
        this.timeCreated = timeCreated;

        key = new HashMap<>();
        key.put("id", new AttributeValue(id));
        key.put("item_type", new AttributeValue(itemType));

        this.attributes = new HashMap<>();
        this.attributes.putAll(attributes);

        item = new HashMap<>();
        item.putAll(key);
        item.putAll(attributes);
    }

    public DatabaseObject(Map<String, AttributeValue> item) {
        try {
            this.id = item.get("id").getS();
            this.itemType = item.get("item_type").getS();
            this.marker = item.get("marker").getN();
            this.timeCreated = item.get("time_created").getS();
        }
        catch (Exception e) {
            this.id = null;
            this.itemType = null;
            this.marker = null;
            this.timeCreated = null;

            return;
        }

        this.item = item;
        this.key = new HashMap<>();
        this.attributes = new HashMap<>();

        // https://stackoverflow.com/a/9009709
        for(Map.Entry<String, AttributeValue> entry : item.entrySet()) {
            String key = entry.getKey();
            AttributeValue value = entry.getValue();
            if (key.equals("id") || key.equals("item_type")) {
                this.key.put(key, value);
            }
            else {
                this.attributes.put(key, value);
            }
        }
    }

    public void putAttribute(String key, AttributeValue value) {
        // You have to also put it into the item
        attributes.put(key, value);
        item.put(key, value);
    }
}
