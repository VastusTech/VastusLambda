package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import main.java.databaseOperations.exceptions.CorruptedItemException;

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

    /**
     * The main constructor for the Review class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Review(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Review")) throw new CorruptedItemException("Review initialized for wrong item type");
        this.by = item.getString("by");
        this.about = item.getString("about");
        this.friendlinessRating = Float.parseFloat(item.getString("friendlinessRating"));
        this.effectivenessRating = Float.parseFloat(item.getString("effectivenessRating"));
        this.reliabilityRating = Float.parseFloat(item.getString("reliabilityRating"));
        this.overallRating = (friendlinessRating + effectivenessRating + reliabilityRating) / 3.0f;
        this.description = item.getString("description");
    }

    /**
     * Gets the empty item with the default values for the Review object.
     *
     * @return The map of attribute values for the item.
     */
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

    /**
     * Reads a Review from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Review object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Review readReview(String id) throws Exception {
        return (Review) read(getTableName(), getPrimaryKey("Review", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Review) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Review)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        return super.getObjectFieldsList();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), by, about, friendlinessRating, effectivenessRating, reliabilityRating, description);
    }
}
