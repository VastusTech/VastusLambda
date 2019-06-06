package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;

import java.util.Map;

import main.java.databaseObjects.DatabaseObject;
import main.java.databaseOperations.exceptions.BadIDException;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.Constants;
import main.java.logic.TimeHelper;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseObjectTest {
    private Item getDatabaseObjectItem() {
        return new Item()
                .withString("item_type", "Client")
                .withString("id", "CL0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString());
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testGetEmptyItem() {
        Map<String, AttributeValue> emptyItem = DatabaseObject.getEmptyItem();
        assertEquals(0, Integer.parseInt(emptyItem.get("marker").getN()));
        assertNull(emptyItem.get("item_type"));
        assertNull(emptyItem.get("id"));
        assertNull(emptyItem.get("time_created"));
    }

    @Test
    public void testGetPrimaryKey() {
        assertEquals(new PrimaryKey("item_type", "Challenge", "id", "CH0001"),
                DatabaseObject.getPrimaryKey("Challenge", "CH0001"));
    }

    @Test
    public void testGetTableName() {
        assertEquals(Constants.databaseTableName, DatabaseObject.getTableName());
    }

    @Test
    public void testDatabaseObject() throws Exception {
        DatabaseObject object = new DatabaseObject(getDatabaseObjectItem());
        assertEquals("Client", object.itemType);
        assertEquals("CL0001", object.id);
        assertEquals(1, object.marker);
        assertNotNull(object.timeCreated);
    }

    @Test
    public void testDatabaseObjectWithHighMarker() throws Exception {
        DatabaseObject object = new DatabaseObject(getDatabaseObjectItem()
                .withNumber("marker", 100));
        assertEquals(100, object.marker);
    }

    @Test
    public void testDatabaseObjectDifferentItemType() throws Exception {
        DatabaseObject object = new DatabaseObject(getDatabaseObjectItem()
                .withString("item_type", "Challenge")
                .withString("id", "CH0001"));
        assertEquals("Challenge", object.itemType);
        assertEquals("CH0001", object.id);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failNullItemType() throws Exception {
        new DatabaseObject(getDatabaseObjectItem().with("item_type", null));
    }

    @Test(expected = Exception.class)
    public void failBadItemType() throws Exception {
        new DatabaseObject(getDatabaseObjectItem().with("item_type", "Ballenge"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullID() throws Exception {
        new DatabaseObject(getDatabaseObjectItem().with("id", null));
    }

    @Test(expected = BadIDException.class)
    public void failBadID() throws Exception {
        new DatabaseObject(getDatabaseObjectItem().with("id", "~not an item type~"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNotMatchingIDAndItemType() throws Exception {
        new DatabaseObject(getDatabaseObjectItem().withString("id", "CH0001")
                .withString("item_type", "Client"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullMarker() throws Exception {
        new DatabaseObject(getDatabaseObjectItem().with("marker", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNegativeMarker() throws Exception {
        new DatabaseObject(getDatabaseObjectItem().with("marker", -1));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullTimeCreated() throws Exception {
        new DatabaseObject(getDatabaseObjectItem().with("time_created", null));
    }
}
