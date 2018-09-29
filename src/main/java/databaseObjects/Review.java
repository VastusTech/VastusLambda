package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import main.java.Logic.Constants;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ReviewResponse;

import java.util.HashMap;
import java.util.Map;

public class Review extends DatabaseObject {
    public String byID;
    public String aboutID;
    public float friendlinessRating;
    public float effectivenessRating;
    public float reliabilityRating;
    public float overallRating;
    public String description;

    Review(Item item) throws Exception {
        super(item);
        this.byID = item.getString("byID");
        this.aboutID = item.getString("aboutID");
        this.friendlinessRating = Float.parseFloat(item.getString("friendliness_rating"));
        this.effectivenessRating = Float.parseFloat(item.getString("effectiveness_rating"));
        this.reliabilityRating = Float.parseFloat(item.getString("reliability_rating"));
        this.overallRating = (friendlinessRating + effectivenessRating + reliabilityRating) / 3.0f;
        this.description = item.getString("description");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Review"));
        // item.put("byID", new AttributeValue(Constants.nullAttributeValue));
        item.put("byID", null);
        // item.put("aboutID", new AttributeValue(Constants.nullAttributeValue));
        item.put("aboutID", null);
        item.put("friendliness_rating", new AttributeValue("-1.0"));
        item.put("effectiveness_rating", new AttributeValue("-1.0"));
        item.put("reliability_rating", new AttributeValue("-1.0"));
        // item.put("description", new AttributeValue(Constants.nullAttributeValue));
        item.put("description", null);
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new ReviewResponse(this);
    }

    // TODO Implement cache system here again?
    public static Review readReview(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Review"));
        key.put("id", new AttributeValue(id));
        return DynamoDBHandler.getInstance().readItem(key);
    }
}
