package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import main.java.Logic.Constants;
import main.java.Logic.ItemType;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;

import main.java.databaseOperations.DynamoDBHandler;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

abstract public class DatabaseObject extends DatabaseItem {
    final public static String tableName = Constants.databaseTableName;

    public String id;
    public String itemType;
    public int marker;
    public DateTime timeCreated;

    public DatabaseObject(Item item) throws Exception {
        // Set the rest of the object
        this.id = item.getString("id");
        this.itemType = item.getString("item_type");
        // TODO Surely there must be a better way to do this? Test if you just getString?
        this.marker = item.getNumber("marker").intValueExact();
        this.timeCreated = new DateTime(item.getString("timeCreated"));
    }

    static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", null);
        item.put("item_type", null);
        item.put("timeCreated", null);
        item.put("marker", new AttributeValue().withN("0"));
        return item;
    }

    public static DatabaseObject readDatabaseObject(String id, String itemType) throws Exception {
        return (DatabaseObject)DatabaseItem.read(tableName, getPrimaryKey(itemType, id));
    }

    static public PrimaryKey getPrimaryKey(String itemType, String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }
}
