package databaseObjects;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public class Workout extends DatabaseObject {
    Workout(Map<String, AttributeValue> item) {
        super(item);
        // TODO Set the variables
    }
}
