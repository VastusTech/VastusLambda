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
 * Represents a Deal that a Sponsor is offering in exchange for credits.
 */
public class Deal extends DatabaseObject {
    public String sponsor;
    public String productName;
    public String productImagePath;
    public Set<String> productImagePaths;
    public int productCreditPrice;
    public String description;
    public DateTime validUntil;
    public String productStoreLink;
    public int quantity;
    public ProductType productType;
    public Set<String> productsSold;
    public int score;

    /**
     * The main constructor for the Deal class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Deal(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Deal")) throw new CorruptedItemException("Deal initialized for wrong item type");
        this.sponsor = item.getString("sponsor");
        this.productName = item.getString("productName");
        this.productImagePath = item.getString("productImagePath");
        this.productImagePaths = item.getStringSet("productImagePaths");
        if (productImagePaths == null) { productImagePaths = new HashSet<>(); }
        this.productCreditPrice = Integer.parseInt(item.getString("productCreditPrice"));
        this.description = item.getString("description");
        if (item.get("validUntil") == null) { validUntil = null; }
        else { validUntil = new DateTime(item.getString("validUntil")); }
        this.productStoreLink = item.getString("productStoreLink");
        if (item.getNumber("quantity") == null) { quantity = -1; }
        else { quantity = Integer.parseInt(item.getString("quantity")); }
        this.productType = ProductType.valueOf(item.getString("productType"));
        this.productsSold = item.getStringSet("productsSold");
        if (productsSold == null) { productsSold = new HashSet<>(); }
        if (item.getNumber("score") == null) { score = -1; }
        else { this.score = item.getNumber("score").intValueExact(); }
    }

    /**
     * Gets the empty item with the default values for the Deal object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Deal"));
        return item;
    }

    /**
     * Reads a Deal from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Deal object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Deal readDeal(String id) throws Exception {
        return (Deal) read(getTableName(), getPrimaryKey("Deal", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Deal) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Deal)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(productImagePaths, validUntil, productsSold));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sponsor, productName, productImagePath,
                productCreditPrice, description, productStoreLink, quantity, productType, score);
    }

    public enum ProductType {
        coupon,
        electronic,
        physical,
    }
}
