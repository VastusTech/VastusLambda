package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * An Invite is whenever someone is requesting something from someone else, be that to join a
 * Challenge/Event/Group, to invite someone else to join a Challenge/Event/Group, or even to be a
 * friend. :)
 */
public class Invite extends DatabaseObject {
    public String from;
    public String to;
    public String inviteType;
    public String about;
    public String description;

    /**
     * The main constructor for the Invite class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    Invite(Item item) throws Exception {
        super(item);
        this.from = item.getString("from");
        this.to = item.getString("to");
        this.inviteType = item.getString("inviteType");
        this.about = item.getString("about");
        this.description = item.getString("description");
    }

    /**
     * Gets the empty item with the default values for the Invite object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Invite"));
        return item;
    }

    /**
     * Reads an Invite from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Invite object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Invite readInvite(String id) throws Exception {
        return (Invite) read(getTableName(), getPrimaryKey("Invite", id));
    }

    public enum InviteType {
        friendRequest,
        eventInvite,
        challengeInvite,
        groupInvite,
        eventRequest,
        challengeRequest,
        groupRequest
    }
}

