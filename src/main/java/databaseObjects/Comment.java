package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.exceptions.CorruptedItemException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        if (!itemType.equals("Comment")) throw new CorruptedItemException("Comment initialized for wrong item type");
        this.by = item.getString("by");
        this.to = item.getString("to");
        this.comment = item.getString("comment");
        this.likes = item.getStringSet("likes");
        if (likes == null) { likes = new HashSet<>(); }
        this.comments = item.getStringSet("comments");
        if (comments == null) { comments = new HashSet<>(); }
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
        return (Comment) read(getTableName(), getPrimaryKey("Comment", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Comment) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Comment)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(likes, comments));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), by, to, comment);
    }
}
