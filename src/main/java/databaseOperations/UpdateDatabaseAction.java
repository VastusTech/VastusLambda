package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

/**
 * An UpdateDatabaseAction specifies an action that updates an existing item in the database. Also
 * gives the ability to check the values of the item very close to when it is pushed into the
 * database, so that we decrease the chance of any weird database errors.
 */
public class UpdateDatabaseAction extends DatabaseAction {
    /**
     * Initializes a update database action when you know both the ID of the object to update, but
     * you may or may not know the value of the attribute.
     *
     * @param id The ID of the item to update.
     * @param itemType The type of the item to update.
     * @param primaryKey The {@link PrimaryKey} to find the item with.
     * @param attributeName The name of the attribute to update in the item.
     * @param attributeValue The value to update the attribute with.
     * @param ifWithCreate If the update action is with a create and will need to updated.
     * @param action The action to update the item with.
     * @throws Exception If there are any problems with the update database action.
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
     * Initializes a update database action when you know both the ID of the object to update, but
     * you may or may not know the value of the attribute. Also allows you to determine a
     * check handler for if the update is still valid.
     *
     * @param id The ID of the item to update.
     * @param itemType The type of the item to update.
     * @param primaryKey The {@link PrimaryKey} to find the item with.
     * @param attributeName The name of the attribute to update in the item.
     * @param attributeValue The value to update the attribute with.
     * @param ifWithCreate If the update action is with a create and will need to updated.
     * @param action The action to update the item with.
     * @param checkHandler The handler to check the viability of the action.
     * @throws Exception If there are any problems with the update database action.
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

    /**
     * Initializes a update database action when you know the ID of the object to update, but
     * the value needs to be determined by a passover identifier. Also allows you to determine a
     * check handler for if the update is still valid.
     *
     * @param id The id of the item to update.
     * @param itemType The type of the item to update.
     * @param primaryKey The {@link PrimaryKey} to find the item with.
     * @param attributeName The name of the attribute to update in the item.
     * @param passoverIdentifier The passover identifier to identify the value to update with.
     * @param action The action to update the item with.
     * @param checkHandler The handler to check the viability of the action.
     * @throws Exception If there are any problems with the update database action.
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

    /**
     * Initializes a update database action when you don't know the ID of the object to update and
     * must determine it using a passover identifier, but you do know the value of the attribute to
     * update with. Also allows you to determine a check handler for if the update is still valid.
     *
     * @param itemType The type of the item to update.
     * @param passoverIdentifier The passover identifier to get the id of the item to update for.
     * @param attributeName The name of the attribute to update in the item.
     * @param attributeValue The value to update the attribute with.
     * @param action The action to update the item with.
     * @param checkHandler The handler to check the viability of the action.
     * @throws Exception If there are any problems with the update database action.
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
     * Initializes a update database action when you don't know the ID of the object to update and
     * must determine it using a passover identifier, and you don't know the value of the attribute
     * to update with and it will be determined by the created ID of the compiler. Also allows you
     * to determine a check handler for if the update is still valid.
     *
     * @param itemType The type of the item to update.
     * @param passoverIdentifier The passover identifier to get the id of the item to update for.
     * @param attributeName The name of the attribute to update in the item.
     * @param action The action to update the item with.
     * @param checkHandler The handler to check the viability of the action.
     * @throws Exception If there are any problems with the update database action.
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
     * Initializes the update database action values, checking for improper values and potential
     * typos. Also switches ADD and REMOVE updates to correctly formatted values.
     *
     * @param attributeName The attribute name of the attribute to update.
     * @param attributeValue The {@link AttributeValue} object to update the attribute with.
     * @param action The action to update the attribute with.
     * @throws Exception If there are any errors in the update.
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
     * A value of a specific update action that can be done for an item.
     */
    public enum UpdateAction {
        PUT,
        ADD,
        DELETE,
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof UpdateDatabaseAction) && obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
