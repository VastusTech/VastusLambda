package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;

public class UpdateDatabaseAction extends DatabaseAction {
    public UpdateDatabaseAction(String id, String itemType, String attributeName, AttributeValue attributeValue,
                                boolean ifWithCreate, String action) throws Exception {
        this.action = DBAction.UPDATE;
        this.item = new HashMap<>();
        this.item.put("item_type", new AttributeValue(itemType));
        this.item.put("id", new AttributeValue(id));
        this.updateAttributeName = attributeName;
        this.updateAttribute = attributeValue;
        this.updateAction = action;
        this.ifWithCreate = ifWithCreate;

        if (attributeValue != null) {
            if (attributeValue.getS().equals("")) {
                throw new Exception("The attributeValue is not allowed to be an empty string!!!!");
            }
            if (ifWithCreate) {
                throw new Exception("Don't set the updateAttribute and choose ifWithCreate as true!!!");
            }
        }
        else {
            if (!ifWithCreate) {
                throw new Exception("You have to either set the updateAttribute or choose ifWithCreate as true!!");
            }
        }
    }

    public UpdateDatabaseAction(String id, String itemType, String attributeName, AttributeValue attributeValue,
                                boolean ifWithCreate, String action, CheckHandler checkHandler) throws Exception {
        this.action = DBAction.UPDATESAFE;
        this.item = new HashMap<>();
        this.item.put("item_type", new AttributeValue(itemType));
        this.item.put("id", new AttributeValue(id));
        this.updateAttributeName = attributeName;
        this.updateAttribute = attributeValue;
        this.updateAction = action;
        this.ifWithCreate = ifWithCreate;
        this.checkHandler = checkHandler;

        if (attributeValue.getS().equals("")) {
            throw new Exception("The attributeValue is not allowed to be an empty string!!!!");
        }

        if (ifWithCreate && updateAttribute != null) {
            throw new Exception("Don't set the updateAttribute and choose ifWithCreate as true!!!");
        }
    }
}
