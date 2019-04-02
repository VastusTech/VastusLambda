package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;

import java.util.*;

/**
 * A client is anyone using the app for the Events/Challenges/Groups and other values of the app.
 * They are people who are the primary targets for our product, and they are the main participators
 * in Challenges and following Trainers.
 */
public class Client extends User {
    public Set<String> trainersFollowing;
    public Set<String> subscriptions;

    Client(Item item) throws Exception {
        super(item);
        this.trainersFollowing = item.getStringSet("trainersFollowing");
        if (trainersFollowing == null) { this.trainersFollowing = new HashSet<>(); }
        this.subscriptions = item.getStringSet("subscription");
        if (subscriptions == null) { this.subscriptions = new HashSet<>(); }
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Client"));
        return item;
    }

    // TODO Implement cache system here again?
    public static Client readClient(String id) throws Exception {
        return (Client) read(tableName, getPrimaryKey("Client", id));
    }
}
