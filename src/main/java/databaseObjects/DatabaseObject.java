package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;

import main.java.databaseOperations.exceptions.BadIDException;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.Constants;
import main.java.logic.ItemType;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * DatabaseObject class represents any object that is in the "Classics" table for the Vastus app
 * tech stack. Specifies the essential fields that every item will have, including the fields that
 * make up the primary key: id and item_type.
 */
public class DatabaseObject extends DatabaseItem {
    public String id;
    public String itemType;
    public int marker;
    public DateTime timeCreated;

    /**
     * The main constructor for the DatabaseObject class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public DatabaseObject(Item item) throws Exception {
        // Set the rest of the object
        this.id = item.getString("id");
        if (id == null) throw new CorruptedItemException("ID cannot be null");
        this.itemType = item.getString("item_type");
        if (itemType == null) throw new CorruptedItemException("Item Type cannot be null");
        try { ItemType.valueOf(itemType); } catch (IllegalArgumentException e) { throw new CorruptedItemException("Bad Item Type: " + itemType); }
        if (!ItemType.getItemType(id).equals(itemType)) throw new CorruptedItemException("Item Type does not match ID");
        if (item.getNumber("marker") == null) throw new CorruptedItemException("Marker cannot be null");
        this.marker = item.getNumber("marker").intValueExact();
        if (marker < 0) throw new CorruptedItemException("Marker should not be less than 0");
        this.timeCreated = new DateTime(item.getString("time_created"));
        if (item.getString("time_created") == null) throw new CorruptedItemException("Time Created cannot be null");
    }

    /**
     * Gets the empty item with the default values for the DatabaseObject object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", null);
        item.put("item_type", null);
        item.put("marker", new AttributeValue().withN("0"));
        return item;
    }

    /**
     * Reads a DatabaseObject from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @param itemType The item type of the item to read from the database.
     * @return The DatabaseObject object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static DatabaseObject readDatabaseObject(String id, String itemType) throws Exception {
        return (DatabaseObject)DatabaseItem.read(getTableName(), getPrimaryKey(itemType, id));
    }

    /**
     * Gets the {@link PrimaryKey} to identify the database object.
     *
     * @param itemType The type of the object to read in the database.
     * @param id The ID of the object to read.
     * @return The {@link PrimaryKey} indicating the database object for reading.
     */
    static public PrimaryKey getPrimaryKey(String itemType, String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    /**
     * Gets the table name based on the development status of the application at the moment.
     *
     * @return The name of the table to grab from.
     */
    static public String getTableName() {
        if (Constants.ifDevelopment) {
            return Constants.developmentDatabaseTableName;
        }
        return Constants.databaseTableName;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DatabaseObject) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((DatabaseObject)obj).getObjectFieldsList());
    }

    protected List<Object> getObjectFieldsList() {
        return new ArrayList<>();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemType, marker, timeCreated);
    }
}
