package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import main.java.Logic.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateDatabaseAction extends DatabaseAction {
    public UpdateDatabaseAction(String id, String itemType, String attributeName, AttributeValue attributeValue,
                                boolean ifWithCreate, String action) throws Exception {
        this.action = DBAction.UPDATE;
        this.item = new HashMap<>();
        this.item.put("item_type", new AttributeValue(itemType));
        this.item.put("id", new AttributeValue(id));
        this.ifWithCreate = ifWithCreate;
        this.updateItem = new HashMap<>();

        initWithErrorCheck(attributeName, attributeValue, action);

        // TODO LOG HERE
    }

    public UpdateDatabaseAction(String id, String itemType, String attributeName, AttributeValue attributeValue,
                                boolean ifWithCreate, String action, CheckHandler checkHandler) throws Exception {
        this.action = DBAction.UPDATESAFE;
        this.item = new HashMap<>();
        this.item.put("item_type", new AttributeValue(itemType));
        this.item.put("id", new AttributeValue(id));
        this.ifWithCreate = ifWithCreate;
        this.checkHandler = checkHandler;
        this.updateItem = new HashMap<>();

        initWithErrorCheck(attributeName, attributeValue, action);

        // TODO LOG HERE
    }

    private void initWithErrorCheck(String attributeName, AttributeValue attributeValue, String action) throws Exception {
        if (attributeName != null && (ifWithCreate || attributeValue != null) && action != null) {
            if (!(action.equals("PUT") || action.equals("ADD") || action.equals("DELETE"))) {
                throw new Exception("Invalid action for updating: " + action + "!");
            }

            if (ifWithCreate) {
                this.updateItem.put(attributeName, new AttributeValueUpdate(new AttributeValue("id"), action));
//                if (attributeValue == null) {
//                    this.updateItem.put(attributeName, new AttributeValueUpdate(new AttributeValue("id"), action));
//                }
//                else {
//                    // throw new Exception("INTERNAL ERROR: Don't set the attributeValue and ask it to add the id");
//                }
            } else {
                if ((attributeValue.getS() != null && attributeValue.getS().equals("")) || (attributeValue.getSS() !=
                        null && attributeValue.getSS().size() == 0)) {
                    attributeValue = null;
                }

                if (attributeValue.getS() != null && (action.equals("ADD") || action.equals("DELETE"))) {
                    // This means that the caller is trying to put a single string in the field, let's help them out
                    List<String> stringList = new ArrayList<>();
                    stringList.add(attributeValue.getS());
                    this.updateItem.put(attributeName, new AttributeValueUpdate(new AttributeValue(stringList), action));
                }
                else {
                    this.updateItem.put(attributeName, new AttributeValueUpdate(attributeValue, action));
                }
            }
        }
    }
}
