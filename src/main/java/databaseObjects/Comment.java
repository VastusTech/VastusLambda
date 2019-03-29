package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * TODO
 */
public class Comment extends DatabaseObject {
    public String by;
    public String to;
    public String comment;
    public Set<String> comments;

    public Comment(Item item) throws Exception {
        super(item);
        this.by = item.getString("by");
        this.to = item.getString("to");
        this.comment = item.getString("comment");
        this.comments = item.getStringSet("comments");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Comment"));
        return item;
    }

    // TODO Implement cache system here again?
    public static Comment readComment(String id) throws Exception {
        return (Comment) read(tableName, getPrimaryKey("Comment", id));
    }
}
