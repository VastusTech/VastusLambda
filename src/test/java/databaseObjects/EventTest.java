package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Map;

import main.java.databaseObjects.Event;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EventTest {
    static Item getEventTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Event")
                .withString("id", "EV0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("title", "TITLE")
                .withString("description", "DESCRIPTION")
                .withString("owner", "OWNER")
                .withString("time", getTestTimeInterval().toString())
                .withString("address", "ADDRESS")
                .withStringSet("members", Sets.newSet("1", "2", "3"))
                .withStringSet("invitedMembers", Sets.newSet("1", "2", "3"))
                .withStringSet("memberRequests", Sets.newSet("1", "2", "3"))
                .withStringSet("receivedInvites", Sets.newSet("1", "2", "3"))
                .withString("capacity", "1000")
                .withString("ifCompleted", "false")
                .withString("access", "ACCESS")
                .withString("restriction", "RESTRICTION")
                .withString("challenge", "CHALLENGE")
                .withString("group", "GROUP")
                .withStringSet("tags", Sets.newSet("1", "2", "3"));
    }

    private static TimeInterval getTestTimeInterval() throws Exception {
        return new TimeInterval(TimeHelper.isoString(new DateTime(2000, 10, 5, 12, 0))
                + "_" + TimeHelper.isoString(new DateTime(2000, 10, 5, 13, 0)));
    }

    private void assertEmptySet(Object actual) {
        assertEquals(Sets.newSet(), actual);
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testGetEmptyItem() {
        Map<String, AttributeValue> emptyItem = Event.getEmptyItem();
        assertEquals("Event", emptyItem.get("item_type").getS());
        assertEquals("false", emptyItem.get("ifCompleted").getS());
    }

    @Test
    public void testEvent() throws Exception {
        Event event = new Event(getEventTestItem());
        assertEquals("Event", event.itemType);
        assertEquals("EV0001", event.id);
        assertEquals(1, event.marker);
        assertNotNull(event.timeCreated);
        assertEquals("TITLE", event.title);
        assertEquals("DESCRIPTION", event.description);
        assertEquals("OWNER", event.owner);
        assertEquals(getTestTimeInterval(), event.time);
        assertEquals("ADDRESS", event.address);
        assertEquals(Sets.newSet("1", "2", "3"), event.members);
        assertEquals(Sets.newSet("1", "2", "3"), event.invitedMembers);
        assertEquals(Sets.newSet("1", "2", "3"), event.memberRequests);
        assertEquals(Sets.newSet("1", "2", "3"), event.receivedInvites);
        assertEquals(1000, event.capacity);
        assertFalse(event.ifCompleted);
        assertEquals("ACCESS", event.access);
        assertEquals("RESTRICTION", event.restriction);
        assertEquals("CHALLENGE", event.challenge);
        assertEquals("GROUP", event.group);
        assertEquals(Sets.newSet("1", "2", "3"), event.tags);
    }

    @Test
    public void testEventNullDescription() throws Exception {
        assertNull((new Event(getEventTestItem().with("description", null))).description);
    }

    @Test
    public void testEventNullMembers() throws Exception {
        assertEmptySet((new Event(getEventTestItem().with("members", null))).members);
    }

    @Test
    public void testEventNullInvitedMembers() throws Exception {
        assertEmptySet((new Event(getEventTestItem().with("invitedMembers", null))).invitedMembers);
    }

    @Test
    public void testEventNullMemberRequests() throws Exception {
        assertEmptySet((new Event(getEventTestItem().with("memberRequests", null))).memberRequests);
    }

    @Test
    public void testEventNullReceivedInvites() throws Exception {
        assertEmptySet((new Event(getEventTestItem().with("receivedInvites", null))).receivedInvites);
    }

    @Test
    public void testEventNullRestriction() throws Exception {
        assertNull((new Event(getEventTestItem().with("restriction", null))).restriction);
    }

    @Test
    public void testEventNullChallenge() throws Exception {
        assertNull((new Event(getEventTestItem().with("challenge", null))).challenge);
    }

    @Test
    public void testEventNullGroup() throws Exception {
        assertNull((new Event(getEventTestItem().with("group", null))).group);
    }

    @Test
    public void testEventNullTags() throws Exception {
        assertEmptySet((new Event(getEventTestItem().with("tags", null))).tags);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failEventWrongItemTypeAndID() throws Exception {
        new Event(getEventTestItem().withString("item_type", "Trainer")
                .withString("id", "TR0001"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failEventNullOwner() throws Exception {
        new Event(getEventTestItem().with("owner", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failEventNullTitle() throws Exception {
        new Event(getEventTestItem().with("title", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failEventNullTime() throws Exception {
        new Event(getEventTestItem().with("time", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failEventNullCapacity() throws Exception {
        new Event(getEventTestItem().with("capacity", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failEventBadCapacity() throws Exception {
        new Event(getEventTestItem().withString("capacity", "not-a-capacity"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failEventLowCapacity() throws Exception {
        new Event(getEventTestItem().withString("capacity", "0"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failEventNullAccess() throws Exception {
        new Event(getEventTestItem().with("access", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failEventNullIfCompleted() throws Exception {
        new Event(getEventTestItem().with("ifCompleted", null));
    }
}
