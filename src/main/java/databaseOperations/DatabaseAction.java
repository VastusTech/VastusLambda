package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;

import java.util.Map;

public abstract class DatabaseAction {
    // This defines what it's doing to the database (CRUD)
    DBAction action;
    Map<String, AttributeValue> item;

    // These should only be used for UPDATE and UPDATESAFE main.java.databaseOperations.DBAction's
    // String updateAttributeName;
    // AttributeValue updateAttribute;
    // String updateAction;

    Map<String, AttributeValueUpdate> updateItem;

    // This should only be used for SAFE methods
    CheckHandler checkHandler;

    // This determines whether the ID will be given through item or through the CREATE statment
    boolean ifWithCreate;

    public enum UpdateAction {
        PUT,
        ADD,
        DELETE
    }
}
