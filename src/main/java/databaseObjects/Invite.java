package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.exceptions.CorruptedItemException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An Invite is whenever someone is requesting something from someone else, be that to join a
 * Challenge/Event/Group, to invite someone else to join a Challenge/Event/Group, or even to be a
 * friend. :)
 */
public class Invite extends DatabaseObject {
    public String from;
    public String to;
    public InviteType inviteType;
    public String about;
    public String description;

    /**
     * The main constructor for the Invite class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Invite(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Invite")) throw new CorruptedItemException("Invite initialized for wrong item type");
        this.from = item.getString("from");
        if (from == null) throw new CorruptedItemException("From may not be null");
        this.to = item.getString("to");
        if (to == null) throw new CorruptedItemException("To may not be null");
        if (item.getString("inviteType") == null) throw new CorruptedItemException("Invite Type may not be null");
        try { this.inviteType = InviteType.valueOf(item.getString("inviteType")); }
        catch (IllegalArgumentException e) {
            throw new CorruptedItemException("Unrecognized Invite Type", e);
        }
        this.about = item.getString("about");
        if (about == null) throw new CorruptedItemException("About may not be null");
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

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Invite) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Invite)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        return super.getObjectFieldsList();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to, inviteType, about, description);
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

