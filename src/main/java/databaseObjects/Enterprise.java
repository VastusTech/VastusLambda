package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

/**
 * An Enterprise Object represents a Company that is signing up their entire staff to be on the app,
 * this is a half-baked concept right now, but TODO finish the design of this?
 */
public class Enterprise extends DatabaseObject {
    public Enterprise(Item item) throws Exception {
        super(item);
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        return item;
    }

    public static Enterprise readEnterprise(String id) throws Exception {
        return (Enterprise) read(tableName, getPrimaryKey("Enterprise", id));
    }
}
