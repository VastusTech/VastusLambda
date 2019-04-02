package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Sponsor is meant to represent a User on the app that will want to represent a product or brand
 * that will be advertised on our platform. TODO A lot of details still to figure out here.
 */
public class Sponsor extends User {
    Sponsor(Item item) throws Exception {
        super(item);
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Sponsor"));
        return item;
    }

    // TODO Implement cache system here again?
    public static Sponsor readSponsor(String id) throws Exception {
        return (Sponsor) read(tableName, getPrimaryKey("Sponsor", id));
    }

//    public static Client querySponsor(String username) throws Exception {
//        return DynamoDBHandler.getInstance().usernameQuery(username, "Sponsor");
//    }
}
