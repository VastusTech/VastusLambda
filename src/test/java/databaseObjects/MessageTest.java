package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Map;

import main.java.databaseObjects.Message;
import main.java.databaseOperations.exceptions.BadIDException;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.Constants;
import main.java.logic.TimeHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MessageTest {
    static Item getMessageTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Message")
                .withString("id", "ME0001")
                .withString("board", "BOARD")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("from", "FROM")
                .withString("type", "TYPE")
                .withString("name", "NAME")
                .withString("profileImagePath", "PROFILEIMAGEPATH")
                .withString("message", "MESSAGE")
                .withStringSet("lastSeenFor", Sets.newSet("1", "2", "3"));
    }

    private void assertEmptySet(Object actual) {
        assertEquals(Sets.newSet(), actual);
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testGetEmptyItem() {
        Map<String, AttributeValue> emptyItem = Message.getEmptyItem();
        assertEquals("Message", emptyItem.get("item_type").getS());
        assertEquals("0", emptyItem.get("marker").getN());
    }

    @Test
    public void testGetPrimaryKey() {
        assertEquals(new PrimaryKey("board", "BOARD", "id", "ME0001"),
                Message.getPrimaryKey("BOARD", "ME0001"));
    }

    @Test
    public void testGetTableName() {
        assertEquals(Constants.messageTableName, Message.getTableName());
    }

    @Test
    public void testMessage() throws Exception {
        Message message = new Message(getMessageTestItem());
        assertEquals("Message", message.itemType);
        assertEquals("ME0001", message.id);
        assertEquals("BOARD", message.board);
        assertEquals(1, message.marker);
        assertNotNull(message.timeCreated);
        assertEquals("FROM", message.from);
        assertEquals("TYPE", message.type);
        assertEquals("NAME", message.name);
        assertEquals("PROFILEIMAGEPATH", message.profileImagePath);
        assertEquals("MESSAGE", message.message);
        assertEquals(Sets.newSet("1", "2", "3"), message.lastSeenFor);
    }

    @Test
    public void testMessageWithHighMarker() throws Exception {
        assertEquals(100, (new Message(getMessageTestItem().withNumber("marker", 100))).marker);
    }

    @Test
    public void testMessageNullType() throws Exception {
        assertNull((new Message(getMessageTestItem().with("type", null))).type);
    }

    @Test
    public void testMessageNullName() throws Exception {
        assertNull((new Message(getMessageTestItem().with("name", null))).name);
    }

    @Test
    public void testMessageNullProfileImagePath() throws Exception {
        assertNull((new Message(getMessageTestItem().with("profileImagePath", null))).profileImagePath);
    }

    @Test
    public void testMessageNullMessage() throws Exception {
        assertNull((new Message(getMessageTestItem().with("message", null))).message);
    }

    @Test
    public void testMessageNullLastSeenFor() throws Exception {
        assertEmptySet((new Message(getMessageTestItem().with("lastSeenFor", null))).lastSeenFor);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================


    @Test(expected = CorruptedItemException.class)
    public void failNullItemType() throws Exception {
        new Message(getMessageTestItem().with("item_type", null));
    }

    @Test(expected = Exception.class)
    public void failBadItemType() throws Exception {
        new Message(getMessageTestItem().with("item_type", "Fessage"));
    }

    @Test(expected = Exception.class)
    public void failWrongItemType() throws Exception {
        new Message(getMessageTestItem().with("item_type", "Client"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullID() throws Exception {
        new Message(getMessageTestItem().with("id", null));
    }

    @Test(expected = BadIDException.class)
    public void failBadID() throws Exception {
        new Message(getMessageTestItem().with("id", "~not an item type~"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failWrongID() throws Exception {
        new Message(getMessageTestItem().with("id", "CL0001"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failClientWrongItemTypeAndID() throws Exception {
        new Message(getMessageTestItem().withString("item_type", "Trainer")
                .withString("id", "TR0001"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullMarker() throws Exception {
        new Message(getMessageTestItem().with("marker", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNegativeMarker() throws Exception {
        new Message(getMessageTestItem().with("marker", -1));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullTimeCreated() throws Exception {
        new Message(getMessageTestItem().with("time_created", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullBoard() throws Exception {
        new Message(getMessageTestItem().with("board", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullFrom() throws Exception {
        new Message(getMessageTestItem().with("from", null));
    }
}
