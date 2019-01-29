package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    Post(Item item) throws Exception {
        super(item);
        this.by = item.getString("by");
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

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Post"));
        item.put("access", new AttributeValue("private"));
        item.put("likes", new AttributeValue().withN("0"));
        return item;
    }

    // TODO Implement cache system here again?
    public static Post readPost(String id) throws Exception {
        return (Post) read(tableName, getPrimaryKey("Post", id));
    }
}
