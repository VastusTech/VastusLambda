package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

/**
 * A Review is meant to represent a rating and description of another User by a User.
 */
public class Review extends DatabaseObject {
    public String by;
    public String about;
    public float friendlinessRating;
    public float effectivenessRating;
    public float reliabilityRating;
    public float overallRating;
    public String description;

    Review(Item item) throws Exception {
        super(item);
        this.by = item.getString("by");
        this.about = item.getString("about");
        this.friendlinessRating = Float.parseFloat(item.getString("friendlinessRating"));
        this.effectivenessRating = Float.parseFloat(item.getString("effectivenessRating"));
        this.reliabilityRating = Float.parseFloat(item.getString("reliabilityRating"));
        this.overallRating = (friendlinessRating + effectivenessRating + reliabilityRating) / 3.0f;
        this.description = item.getString("description");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Review"));
        // item.put("byID", new AttributeValue(Constants.nullAttributeValue));
        // item.put("byID", null);
        // item.put("aboutID", new AttributeValue(Constants.nullAttributeValue));
        // item.put("aboutID", null);
        item.put("friendlinessRating", new AttributeValue("0.0"));
        item.put("effectivenessRating", new AttributeValue("0.0"));
        item.put("reliabilityRating", new AttributeValue("0.0"));
        // item.put("description", new AttributeValue(Constants.nullAttributeValue));
        // item.put("description", null);
        return item;
    }

    // TODO Implement cache system here again?
    public static Review readReview(String id) throws Exception {
        return (Review) read(tableName, getPrimaryKey("Review", id));
    }
}
