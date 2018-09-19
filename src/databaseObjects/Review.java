package databaseObjects;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public class Review extends DatabaseObject {
    Review(Map<String, AttributeValue> item) {
        super(item);
        // TODO Set the variables
    }
}
