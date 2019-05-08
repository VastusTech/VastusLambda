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

    /**
     * The main constructor for the Client class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    Client(Item item) throws Exception {
        super(item);
        this.trainersFollowing = item.getStringSet("trainersFollowing");
        if (trainersFollowing == null) { this.trainersFollowing = new HashSet<>(); }
        this.subscriptions = item.getStringSet("subscription");
        if (subscriptions == null) { this.subscriptions = new HashSet<>(); }
    }

    /**
     * Gets the empty item with the default values for the Client object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Client"));
        return item;
    }

    /**
     * Reads a Client from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Client object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Client readClient(String id) throws Exception {
        return (Client) read(getTableName(), getPrimaryKey("Client", id));
    }
}
