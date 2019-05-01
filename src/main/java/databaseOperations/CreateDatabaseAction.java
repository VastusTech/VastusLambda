package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * A CreateDatabaseAction represents a request to the database to create a new item. This also gives
 * the power to update with the ID once it has been decided.
 */
public class CreateDatabaseAction extends DatabaseAction {
    public UpdateWithIDHandler updateWithIDHandler;

    /**
     * TODO
     *
     * @param itemType
     * @param item
     * @param passoverIdentifiers
     * @param updateWithIDHandler
     */
    public CreateDatabaseAction(String itemType, Map<String, AttributeValue> item,
                                Map<String, String> passoverIdentifiers, UpdateWithIDHandler
                                        updateWithIDHandler) {
        if (passoverIdentifiers != null) {
            this.ifWithCreate = true;
            this.passoverIdentifiers = passoverIdentifiers;
        }
        else {
            this.ifWithCreate = false;
            this.passoverIdentifiers = new HashMap<>();
        }
        action = DBAction.CREATE;
        this.itemType = itemType;
        this.item = new HashMap<>();
        for (Map.Entry<String, AttributeValue> entry: item.entrySet()) {
            String key = entry.getKey();
            AttributeValue attributeValue = entry.getValue();
            if (attributeValue != null) {
                if ((!this.ifWithCreate && attributeValue.getS() != null && attributeValue.getS().equals(""))
                        || (attributeValue.getSS() != null && attributeValue.getSS().size() == 0)) {
                    this.item.put(key, null);
                }
                else {
                    this.item.put(key, attributeValue);
                }
            }
        }
        this.updateWithIDHandler = updateWithIDHandler;
    }
}
