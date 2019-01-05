package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.Logic.Constants;

import java.util.HashMap;
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

    public enum MessageType {
        picture,
        video
    }
}
