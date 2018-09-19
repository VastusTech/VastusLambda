package databaseObjects;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public class Gym extends User {
    Gym(Map<String, AttributeValue> item) {
        super(item);
        // TODO Set the variables
    }
}
