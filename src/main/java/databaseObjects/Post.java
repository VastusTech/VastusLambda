package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.exceptions.CorruptedItemException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A Post is an amount of content that a User has posted out for others to see. It can be sharing
 * other objects, using it's own description, and including pictures and videos.
 */
public class Post extends DatabaseObject {
    public String by;
    public String description;
    public String about;
    public String access;
    public String postType;
    public Set<String> picturePaths;
    public Set<String> videoPaths;
    public Set<String> likes;
    public Set<String> comments;
    public String group;

    /**
     * The main constructor for the Post class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Post(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Post")) throw new CorruptedItemException("Post initialized for wrong item type");
        this.by = item.getString("by");
        if (by == null) throw new CorruptedItemException("By cannot be null");
        this.description = item.getString("description");
        this.about = item.getString("about");
        this.access = item.getString("access");
        this.postType = item.getString("postType");
        this.picturePaths = item.getStringSet("picturePaths");
        if (picturePaths == null) { this.picturePaths = new HashSet<>(); }
        this.videoPaths = item.getStringSet("videoPaths");
        if (videoPaths == null) { this.videoPaths = new HashSet<>(); }
        this.likes = item.getStringSet("likes");
        if (likes == null) { this.likes = new HashSet<>(); }
        this.comments = item.getStringSet("comments");
        if (comments == null) { this.comments = new HashSet<>(); }
        this.group = item.getString("group");
    }

    /**
     * Gets the empty item with the default values for the Post object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Post"));
        return item;
    }

    /**
     * Reads a Post from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Post object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Post readPost(String id) throws Exception {
        return (Post) read(getTableName(), getPrimaryKey("Post", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Post) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Post)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(picturePaths, videoPaths, likes, comments));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), by, description, about, access, postType, group);
    }
}
