package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.Constants;
import main.java.logic.ItemType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A Message is a direct message to a specific message board. Messages are housed on a different
 * table to make queries on Messages faster and more reliable. They can include a video or a picture
 * as well.
 */
public class Message extends DatabaseItem {
    public String id;
    public String board;
    public String itemType;
    public int marker;
    public String timeCreated;
    public String from;
    public String type;
    public String name;
    public String profileImagePath;
    public String message;
    public Set<String> lastSeenFor;

    /**
     * The main constructor for the Message class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Message(Item item) throws Exception {
        id = item.getString("id");
        if (id == null) throw new CorruptedItemException("ID cannot be null");
        board = item.getString("board");
        if (board == null) throw new CorruptedItemException("Board cannot be null");
        itemType = item.getString("item_type");
        if (itemType == null) throw new CorruptedItemException("Item Type cannot be null");
        if (!ItemType.getItemType(id).equals("Message")) throw new CorruptedItemException("Mismatched item type for Message");
        if (!itemType.equals("Message")) throw new CorruptedItemException("Message initialized for wrong item type");
        if (item.getNumber("marker") == null) throw new CorruptedItemException("Marker cannot be null");
        marker = item.getNumber("marker").intValueExact();
        if (marker < 0) throw new CorruptedItemException("Marker should not be less than 0");
        timeCreated = item.getString("time_created");
        if (item.getString("time_created") == null) throw new CorruptedItemException("Time Created cannot be null");
        from = item.getString("from");
        if (from == null) throw new CorruptedItemException("From cannot be null");
        type = item.getString("type");
        name = item.getString("name");
        profileImagePath = item.getString("profileImagePath");
        message = item.getString("message");
        lastSeenFor = item.getStringSet("lastSeenFor");
        if (lastSeenFor == null) { lastSeenFor = new HashSet<>(); }
    }

    /**
     * Gets the empty item with the default values for the Message object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Message"));
        key.put("marker", new AttributeValue().withN("0"));
        return key;
    }

    /**
     * Reads a Message from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param board The name of the board containing the Message object.
     * @param id The ID to read from the database.
     * @return The Message object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    static public Message readMessage(String board, String id) throws Exception {
        return (Message)read(getTableName(), getPrimaryKey(board, id));
    }

    /**
     * Gets the {@link PrimaryKey} to identify the Message object.
     *
     * @param board The board containing the Message object.
     * @param id The ID of the Message to fetch.
     * @return The {@link PrimaryKey} indicating the database object for reading.
     */
    static public PrimaryKey getPrimaryKey(String board, String id) {
        return new PrimaryKey("board", board, "id", id);
    }

    /**
     * Gets the IDs of the Users to notify about the creation of a message based on where it is
     * being sent to.
     *
     * @param board The name of the board to send the Message to.
     * @return The list of User IDs to notify about the creation of the board.
     * @throws Exception If the board name is malformed.
     */
    static public List<String> getNotificationIDsFromBoard(String board) throws Exception {
        List<String> sendIDs = new ArrayList<>();
        String[] ids = board.split("_");
        if (ids.length == 1) {
            // Then this must be a event ID or a challenge ID
            String id = ids[0];
            String itemType = ItemType.getItemType(id);
            // Only send it to the owners
            if (itemType.equals("Group")) {
                sendIDs.addAll(Group.readGroup(id).owners);
            }
            else if (itemType.equals("Challenge")) {
                sendIDs.add(Challenge.readChallenge(id).owner);
            }
            else if (itemType.equals("Event")) {
                sendIDs.add(Event.readEvent(id).owner);
            }
            else {
                throw new Exception("For a chat with only one ID, that ID must be either an event or a challenge!");
            }
        }
        else if (ids.length > 1) {
            // Then this must be all user IDs
            for (String id : ids) {
                // Check the ID
                User.readUser(id, ItemType.getItemType(id));
                sendIDs.add(id.trim());
            }
        }
        else {
            throw new Exception("Board contains no IDs!");
        }
        return sendIDs;
    }

    /**
     * Gets the table name based on the development status of the application at the moment.
     *
     * @return The name of the table to grab from.
     */
    static public String getTableName() {
        if (Constants.ifDevelopment) {
            return Constants.developmentMessageTableName;
        }
        return Constants.messageTableName;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Message) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Message)obj).getObjectFieldsList());
    }

    protected List<Object> getObjectFieldsList() {
        return Collections.singletonList(lastSeenFor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, board, itemType, marker, timeCreated, from, type, name,
                profileImagePath, message);
    }

    /**
     * The values of types of messages that a Message object can be.
     */
    public enum MessageType {
        picture,
        video
    }
}
