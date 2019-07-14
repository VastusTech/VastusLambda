package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.List;
import java.util.Map;

import main.java.databaseOperations.exceptions.CorruptedItemException;

public class Admin extends User {
    /**
     * The main constructor for the Admin class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Admin(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Admin")) throw new CorruptedItemException("Admin initialized for wrong item type");
    }

    /**
     * Gets the empty item with the default values for the Admin object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Admin"));
        return item;
    }

    /**
     * Reads a Admin from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Admin object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Admin readAdmin(String id) throws Exception {
        return (Admin) read(getTableName(), getPrimaryKey("Admin", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Admin) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Admin)obj).getObjectFieldsList());
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
