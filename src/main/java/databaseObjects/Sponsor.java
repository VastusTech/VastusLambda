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

    /**
     * The main constructor for the Sponsor class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    Sponsor(Item item) throws Exception {
        super(item);
    }

    /**
     * Gets the empty item with the default values for the Sponsor object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Sponsor"));
        return item;
    }

    /**
     * Reads a Sponsor from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Sponsor object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Sponsor readSponsor(String id) throws Exception {
        return (Sponsor) read(getTableName(), getPrimaryKey("Sponsor", id));
    }

//    public static Client querySponsor(String username) throws Exception {
//        return DynamoDBHandler.getInstance().usernameQuery(username, "Sponsor");
//    }
}
