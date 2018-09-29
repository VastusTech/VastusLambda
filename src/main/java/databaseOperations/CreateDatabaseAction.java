package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public class CreateDatabaseAction extends DatabaseAction {
    public CreateDatabaseAction(Map<String, AttributeValue> item) {
        action = DBAction.CREATE;
        this.item = new HashMap<>();
        for (Map.Entry<String, AttributeValue> entry: item.entrySet()) {
            String key = entry.getKey();
            AttributeValue attributeValue = entry.getValue();
            if (attributeValue != null) {
                if ((attributeValue.getS() != null && attributeValue.getS().equals("")) || (attributeValue.getSS() !=
                        null && attributeValue.getSS().size() == 0)) {
                    this.item.put(key, null);
                }
                else {
                    this.item.put(key, attributeValue);
                }
            }
        }
    }
}
