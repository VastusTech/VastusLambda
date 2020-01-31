package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import main.java.databaseOperations.exceptions.CorruptedItemException;

/**
 * TODO
 */
public class Product extends DatabaseObject {
    public String owner;
    public String deal;
    public DateTime expirationDate;
    public Set<String> codes;
    public Set<String> links;

    /**
     * The main constructor for the Product class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Product(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Product")) throw new CorruptedItemException("Product initialized for wrong item type");
        this.owner = item.getString(item.getString("owner"));
        this.deal = item.getString("deal");
        this.expirationDate = new DateTime(item.getString("expirationDate"));
        this.codes = item.getStringSet("codes");
        if (codes == null) { codes = new HashSet<>(); }
        this.links = item.getStringSet("links");
        if (links == null) { links = new HashSet<>(); }
    }

    /**
     * Gets the empty item with the default values for the Product object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Product"));
        return item;
    }

    /**
     * Reads a Product from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Product object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Product readProduct(String id) throws Exception {
        return (Product) read(getTableName(), getPrimaryKey("Product", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Product) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Product)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(expirationDate, codes, links));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), owner, deal);
    }
}
