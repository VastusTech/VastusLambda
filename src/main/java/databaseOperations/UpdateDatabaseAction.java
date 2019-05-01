package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

/**
 * An UpdateDatabaseAction specifies an action that updates an existing item in the database. Also
 * gives the ability to check the values of the item very close to when it is pushed into the
 * database, so that we decrease the chance of any weird database errors.
 */
public class UpdateDatabaseAction extends DatabaseAction {
    // Use when you know the ID of the object and the value of the attribute
    // Use when you know the ID of the object, but not of the self ID attribute to update
    /**
     * TODO
     *
     * @param id
     * @param itemType
     * @param primaryKey
     * @param attributeName
     * @param attributeValue
     * @param ifWithCreate
     * @param action
     * @throws Exception
     */
    public UpdateDatabaseAction(String id, String itemType, PrimaryKey primaryKey, String attributeName, AttributeValue attributeValue,
                                boolean ifWithCreate, UpdateAction action) throws Exception {
        this.id = id;
        this.itemType = itemType;
        this.action = DBAction.UPDATE;
        this.primaryKey = primaryKey;
        this.ifWithCreate = ifWithCreate;
        this.passoverIdentifiers = new HashMap<>();
        this.updateItem = new HashMap<>();

        initWithErrorCheck(attributeName, attributeValue, action);
    }

    // Use when you also want to verify something
    /**
     * TODO
     *
     * @param id
     * @param itemType
     * @param primaryKey
     * @param attributeName
     * @param attributeValue
     * @param ifWithCreate
     * @param action
     * @param checkHandler
     * @throws Exception
     */
    public UpdateDatabaseAction(String id, String itemType, PrimaryKey primaryKey, String attributeName, AttributeValue attributeValue,
                                boolean ifWithCreate, UpdateAction action, CheckHandler checkHandler) throws Exception {
        this.id = id;
        this.itemType = itemType;
        this.action = DBAction.UPDATESAFE;
        this.primaryKey = primaryKey;
        this.ifWithCreate = ifWithCreate;
        this.passoverIdentifiers = new HashMap<>();
        this.checkHandler = checkHandler;
        this.updateItem = new HashMap<>();

        initWithErrorCheck(attributeName, attributeValue, action);
    }

    // Use when you know the ID of the object, but not of the passover attribute to update
    /**
     * TODO
     *
     * @param id
     * @param itemType
     * @param primaryKey
     * @param attributeName
     * @param passoverIdentifier
     * @param action
     * @param checkHandler
     * @throws Exception
     */
    public UpdateDatabaseAction(String id, String itemType, PrimaryKey primaryKey, String attributeName,
                                String passoverIdentifier, UpdateAction action, CheckHandler checkHandler) throws Exception {
        this.id = id;
        this.itemType = itemType;
        if (checkHandler != null) {
            this.action = DBAction.UPDATESAFE;
        }
        else {
            this.action = DBAction.UPDATE;
        }
        this.primaryKey = primaryKey;
        this.ifWithCreate = true;
        this.passoverIdentifiers = new HashMap<>();
        this.passoverIdentifiers.put(attributeName, passoverIdentifier);
        this.checkHandler = checkHandler;
        this.updateItem = new HashMap<>();

        initWithErrorCheck(attributeName, new AttributeValue(""), action);
    }

    // Use when you don't know the ID of the object, but you do know the value of the attribute.
    /**
     * TODO
     *
     * @param itemType
     * @param passoverIdentifier
     * @param attributeName
     * @param attributeValue
     * @param action
     * @param checkHandler
     * @throws Exception
     */
    public UpdateDatabaseAction(String itemType, String passoverIdentifier,
                                String attributeName, AttributeValue attributeValue, UpdateAction action, CheckHandler checkHandler) throws Exception {
        this.id = "";
        this.idIdentifier = passoverIdentifier;
        this.itemType = itemType;
        if (checkHandler != null) {
            this.action = DBAction.UPDATESAFE;
        }
        else {
            this.action = DBAction.UPDATE;
        }
        this.primaryKey = null;
        this.ifWithCreate = false;
        this.passoverIdentifiers = new HashMap<>();
        this.checkHandler = checkHandler;
        this.updateItem = new HashMap<>();

        initWithErrorCheck(attributeName, attributeValue, action);
    }

    // User when you don't know the passover ID of the object and you don't know the self ID of
    // the attribute.
    /**
     * TODO
     *
     * @param itemType
     * @param passoverIdentifier
     * @param attributeName
     * @param action
     * @param checkHandler
     * @throws Exception
     */
    public UpdateDatabaseAction(String itemType, String passoverIdentifier, String attributeName, UpdateAction action, CheckHandler checkHandler) throws Exception {
        this.id = "";
        this.idIdentifier = passoverIdentifier;
        this.itemType = itemType;
        if (checkHandler != null) {
            this.action = DBAction.UPDATESAFE;
        }
        else {
            this.action = DBAction.UPDATE;
        }
        this.primaryKey = null;
        this.ifWithCreate = true;
        this.passoverIdentifiers = new HashMap<>();
        this.checkHandler = checkHandler;
        this.updateItem = new HashMap<>();

        initWithErrorCheck(attributeName, new AttributeValue(""), action);
    }

    /**
     * TODO
     *
     * @param attributeName
     * @param attributeValue
     * @param action
     * @throws Exception
     */
    private void initWithErrorCheck(String attributeName, AttributeValue attributeValue, UpdateAction action) throws
            Exception {
        if (attributeName != null && (ifWithCreate || attributeValue != null) && action != null) {
            if (!(action == PUT || action == ADD || action == DELETE)) {
                throw new Exception("Invalid action for updating: " + action + "!");
            }

            if (ifWithCreate) {
                this.updateItem.put(attributeName, new AttributeValueUpdate(new AttributeValue(""), action.toString()));
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
                        this.updateItem.put(attributeName, new AttributeValueUpdate(null, "DELETE"));
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

    /**
     * TODO
     *
     */
    public enum UpdateAction {
        PUT,
        ADD,
        DELETE,
    }
}
