package main.java.databaseObjects;

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

    Review(Map<String, AttributeValue> item) {
        super(item);
        this.byID = item.get("byID").getS();
        this.aboutID = item.get("aboutID").getS();
        this.friendlinessRating = Float.parseFloat(item.get("friendliness_rating").getS());
        this.effectivenessRating = Float.parseFloat(item.get("effectiveness_rating").getS());
        this.reliabilityRating = Float.parseFloat(item.get("reliability_rating").getS());
        this.overallRating = (friendlinessRating + effectivenessRating + reliabilityRating) / 3.0f;
        this.description = item.get("description").getS();
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("byID", new AttributeValue(Constants.nullAttributeValue));
        item.put("aboutID", new AttributeValue(Constants.nullAttributeValue));
        item.put("friendliness_rating", new AttributeValue("-1.0"));
        item.put("effectiveness_rating", new AttributeValue("-1.0"));
        item.put("reliability_rating", new AttributeValue("-1.0"));
        item.put("description", new AttributeValue(Constants.nullAttributeValue));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new ReviewResponse(this);
    }

    public static Review readReview(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Review"));
        key.put("id", new AttributeValue(id));
        return DynamoDBHandler.getInstance().readItem(key);
    }
}
