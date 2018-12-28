package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import main.java.Logic.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class UpdateDatabaseAction extends DatabaseAction {
    public UpdateDatabaseAction(String id, String itemType, String attributeName, AttributeValue attributeValue,
                                boolean ifWithCreate, UpdateAction action) throws Exception {
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
                                boolean ifWithCreate, UpdateAction action, CheckHandler checkHandler) throws Exception {
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

    private void initWithErrorCheck(String attributeName, AttributeValue attributeValue, UpdateAction action) throws
            Exception {
        if (attributeName != null && (ifWithCreate || attributeValue != null) && action != null) {
            if (!(action == PUT || action == ADD || action == DELETE)) {
                throw new Exception("Invalid action for updating: " + action + "!");
            }

            if (ifWithCreate) {
                this.updateItem.put(attributeName, new AttributeValueUpdate(new AttributeValue("id"), action.toString()));
//                if (attributeValue == null) {
//                    this.updateItem.put(attributeName, new AttributeValueUpdate(new AttributeValue("id"), action));
//                }
//                else {
//                    // throw new Exception("INTERNAL ERROR: Don't set the attributeValue and ask it to add the id");
//                }
            } else {
                if ((attributeValue.getS() == null && attributeValue.getSS() == null && attributeValue.getN() ==
                        null) || (attributeValue.getS() != null && attributeValue.getS().equals("")) ||
                                (attributeValue.getSS() != null && attributeValue.getSS().size() == 0)) {
                    attributeValue = null;
                }

                if (attributeValue == null) {
                    if (action == PUT) {
                        // If you're trying to set it to null, then remove the attribute
                        this.updateItem.put(attributeName, new AttributeValueUpdate(null, "REMOVE"));
                    }
                    else {
                        throw new Exception("Cannot add/remove a null value to/from a set");
                    }
                }
                else {
                    if (attributeValue.getS() != null && (action == ADD || action == DELETE)) {
                        // This means that the caller is trying to put a single string in the field, let's help them out
                        List<String> stringList = new ArrayList<>();
                        stringList.add(attributeValue.getS());
                        this.updateItem.put(attributeName, new AttributeValueUpdate(new AttributeValue(stringList),
                                action.toString()));
                    }
                    else {
                        this.updateItem.put(attributeName, new AttributeValueUpdate(attributeValue, action.toString()));
                    }
                }
            }
        }
    }

    public enum UpdateAction {
        PUT,
        ADD,
        DELETE,
    }
}
