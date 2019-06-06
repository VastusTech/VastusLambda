package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import main.java.databaseOperations.exceptions.CorruptedItemException;

/**
 * An Enterprise Object represents a Company that is signing up their entire staff to be on the app,
 * this is a half-baked concept right now, but TODO finish the design of this?
 */
public class Enterprise extends DatabaseObject {

    /**
     * The main constructor for the Enterprise class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Enterprise(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Enterprise")) throw new CorruptedItemException("Enterprise initialized for wrong item type");
    }

    /**
     * Gets the empty item with the default values for the Enterprise object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        return item;
    }

    /**
     * Reads a Enterprise from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Enterprise object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Enterprise readEnterprise(String id) throws Exception {
        return (Enterprise) read(getTableName(), getPrimaryKey("Enterprise", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Enterprise) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Enterprise)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        return super.getObjectFieldsList();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
