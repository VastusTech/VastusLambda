package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import main.java.Logic.Constants;

import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseAction {
    // This defines what it's doing to the database (CRUD)
    DBAction action;
    Map<String, AttributeValue> item;
    PrimaryKey primaryKey;
    String id;
    String itemType;

    // These should only be used for UPDATE and UPDATESAFE main.java.databaseOperations.DBAction's
    // String updateAttributeName;
    // AttributeValue updateAttribute;
    // String updateAction;

    Map<String, AttributeValueUpdate> updateItem;

    // This should only be used for SAFE methods
    CheckHandler checkHandler;

    // This determines whether the ID will be given through item or through the CREATE statment
    boolean ifWithCreate;

    // TODO There's gotta be a better way to do this
    public String getTableName() {
        if (itemType != null && itemType.equals("Message")) {
            return Constants.messageTableName;
        }
        return Constants.databaseTableName;
    }

    // TODO IMPORTANT: This only works with primary key attributes that are strings
    public Map<String, AttributeValue> getKey() {
        Map<String, AttributeValue> item = new HashMap<>();
        for (KeyAttribute keyAttribute : primaryKey.getComponents()) {
            item.put(keyAttribute.getName(), new AttributeValue((String)keyAttribute.getValue()));
        }
        return item;
    }
}
