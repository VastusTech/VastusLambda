package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.Logic.Constants;
import main.java.Logic.ItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message extends DatabaseItem {
    final public static String tableName = Constants.messageTableName;

    public String id;
    public String board;
    public String item_type;
    public String time_created;
    public String type;
    public String from;
    public String message;

    public Message(Item item) throws Exception {
        id = item.getString("id");
        board = item.getString("board");
        item_type = item.getString("item_type");
        time_created = item.getString("time_created");
        type = item.getString("type");
        from = item.getString("from");
        message = item.getString("message");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Message"));
        return key;
    }

    static public PrimaryKey getPrimaryKey(String board, String id) {
        return new PrimaryKey("board", board, "id", id);
    }

    static public Message readMessage(String board, String id) throws Exception {
        return (Message)read(tableName, getPrimaryKey(board, id));
    }

    static public List<String> getNotificationIDsFromBoard(String board) throws Exception {
        List<String> sendIDs = new ArrayList<>();
        String[] ids = board.split("_");
        if (ids.length == 1) {
            // Then this must be a event ID or a challenge ID
            String id = ids[0];
            String itemType = ItemType.getItemType(id);
            if (itemType.equals("Challenge")) {
                Challenge challenge = Challenge.readChallenge(id);
                sendIDs.addAll(challenge.members);
            }
            else if (itemType.equals("Event")) {
                Event event = Event.readEvent(id);
                sendIDs.addAll(event.members);
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

    public enum MessageType {
        picture,
        video
    }
}
