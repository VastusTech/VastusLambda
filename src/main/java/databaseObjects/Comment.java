package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Comment is a sub-post off of a Post (or another comment). This is to comment on something that
 * someone else had posted or commented. A lot like every social media's commenting system.
 */
public class Comment extends DatabaseObject {
    public String by;
    public String to;
    public String comment;
    public Set<String> likes;
    public Set<String> comments;

    /**
     * The main constructor for the Comment class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Comment(Item item) throws Exception {
        super(item);
        this.by = item.getString("by");
        this.to = item.getString("to");
        this.comment = item.getString("comment");
        this.likes = item.getStringSet("likes");
        this.comments = item.getStringSet("comments");
    }

    /**
     * Gets the empty item with the default values for the Comment object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Comment"));
        return item;
    }

    /**
     * Reads a Comment from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Comment object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Comment readComment(String id) throws Exception {
        return (Comment) read(tableName, getPrimaryKey("Comment", id));
    }
}
