package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

/**
 * Handles updating the rest of the object in a create statement with values that potentially want
 * to have a reference to its ID. (Example: use it for putting pictures into a S3 Path based on its
 * ID.)
 */
public interface UpdateWithIDHandler {
    /**
     * This java lambda function that edits the item to Create once the ID has been dynamically
     * ascertained.
     *
     * @param item The item for the Create statement, uses {@link AttributeValue} logic for values.
     * @param id The newly created ID to update the statement with.
     * @throws Exception If anything goes wrong during the updating. TODO not a great exception...
     */
    void updateWithID(Map<String, AttributeValue> item, String id) throws Exception;
}
