package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A CreateDatabaseAction represents a request to the database to create a new item. This also gives
 * the power to update with the ID once it has been decided.
 */
public class CreateDatabaseAction extends DatabaseAction {
    public UpdateWithIDHandler updateWithIDHandler;

    /**
     * The main constructor for the CreateDatabaseAction class, indicating what kind of item to add,
     * and allowing the functionality for the Passover of ids.
     *
     * @param itemType The item type for the item to create.
     * @param item The map of attribute names and values to create the item as.
     * @param passoverIdentifiers The map of attribute names to passover identifiers to indicate
     *                            which attributes will need to take the IDs from which passover
     *                            identifier.
     * @param updateWithIDHandler The handler to update the item with once the program has
     *                            determined the ID.
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

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CreateDatabaseAction) && obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
//        return Objects.hash(super.hashCode(), updateWithIDHandler);
    }
}
