package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public interface UpdateWithIDHandler {
    // TODO CHANGE THIS INTO A LAMBDA FUNCTION?
    // This checks to see if the new object read from the database is viable
    void updateWithID(Map<String, AttributeValue> item, String id) throws Exception;
}
