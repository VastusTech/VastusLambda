package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import main.java.logic.Constants;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * DatabaseObject class represents any object that is in the "Classics" table for the Vastus app
 * tech stack. Specifies the essential fields that every item will have, including the fields that
 * make up the primary key: id and item_type.
 */
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
        this.marker = item.getNumber("marker").intValueExact();
        this.timeCreated = new DateTime(item.getString("time_created"));
    }

    static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", null);
        item.put("item_type", null);
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
