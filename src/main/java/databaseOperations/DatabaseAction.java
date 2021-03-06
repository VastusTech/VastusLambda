package main.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import main.java.logic.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A DatabaseAction represents any request to the database to alter anything. Can Create, Update,
 * and Delete, all of which are handled in the sub classes. This class abstracts some of the logic
 * found in all of them.
 */
public abstract class DatabaseAction {
    // This defines what it's doing to the database (CRUD)
    DBAction action;
    Map<String, AttributeValue> item;
    PrimaryKey primaryKey;
    String id;
    String itemType;

    // The ID passover identifier essentially allows the compiler to still compile the actions
    // together when the IDs are not determined yet.
    String idIdentifier = null;

    // The map of passover identifiers identifies which attributes in the item correspond with which
    // passover IDs. To find out which one it belongs to, while finding the "" empty strings that
    // correspond to a placement of an ID, you then have to go into the DatabaseAction's
    // passoverIdentifiers. If it is not in there, then the ID is for the current compiler's created
    // ID. Otherwise, the ID is for the found passover ID.
    // Map<AttributeNameString, PassoverIdentifier>
    Map<String, String> passoverIdentifiers;

    Map<String, AttributeValueUpdate> updateItem;

    // This should only be used for SAFE methods
    CheckHandler checkHandler;

    // This determines whether the ID will be given through item or through the CREATE statment
    boolean ifWithCreate;

    /**
     * Sets the primary key for a
     *
     * @param primaryKey
     */
    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    // TODO There's gotta be a better way to do this
    /**
     * TODO
     *
     * @return
     */
    public String getTableName(boolean ifDevelopment) {
        if (itemType != null && itemType.equals("Message")) {
            return ifDevelopment ? Constants.developmentMessageTableName : Constants.messageTableName;
        }
        return ifDevelopment ? Constants.developmentDatabaseTableName : Constants.databaseTableName;
    }

    // TODO IMPORTANT: This only works with primary key attributes that are strings
    /**
     * TODO
     *
     * @return
     */
    public Map<String, AttributeValue> getKey() {
        Map<String, AttributeValue> item = new HashMap<>();
        for (KeyAttribute keyAttribute : primaryKey.getComponents()) {
            item.put(keyAttribute.getName(), new AttributeValue((String) keyAttribute.getValue()));
        }
        return item;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DatabaseAction) {
            DatabaseAction other = (DatabaseAction) obj;
            return other.hashCode() == hashCode() && Objects.equals(item, other.item)
                    && Objects.equals(passoverIdentifiers, other.passoverIdentifiers)
                    && Objects.equals(updateItem, other.updateItem);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, primaryKey, id, itemType, idIdentifier, ifWithCreate);
    }
}
