package databaseObjects;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public class Client extends User {
    Client(Map<String, AttributeValue> item) {
        super(item);
        // TODO Set the variables
    }
}
