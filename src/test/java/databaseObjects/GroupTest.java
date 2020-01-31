package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Map;

import main.java.databaseObjects.Group;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class GroupTest {
    static Item getGroupTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Group")
                .withString("id", "GR0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("title", "TITLE")
                .withString("description", "DESCRIPTION")
                .withString("motto", "MOTTO")
                .withString("groupImagePath", "GROUPIMAGEPATH")
                .withStringSet("owners", Sets.newSet("1", "2", "3"))
                .withStringSet("members", Sets.newSet("1", "2", "3"))
                .withStringSet("invitedMembers", Sets.newSet("1", "2", "3"))
                .withStringSet("memberRequests", Sets.newSet("1", "2", "3"))
                .withStringSet("receivedInvites", Sets.newSet("1", "2", "3"))
                .withString("access", "ACCESS")
                .withString("restriction", "RESTRICTION")
                .withStringSet("events", Sets.newSet("1", "2", "3"))
                .withStringSet("completedEvents", Sets.newSet("1", "2", "3"))
                .withStringSet("challenges", Sets.newSet("1", "2", "3"))
                .withStringSet("completedChallenges", Sets.newSet("1", "2", "3"))
                .withStringSet("posts", Sets.newSet("1", "2", "3"))
                .withStringSet("tags", Sets.newSet("1", "2", "3"))
                .withStringSet("streaks", Sets.newSet("1", "2", "3"));
    }

    private void assertEmptySet(Object actual) {
        assertEquals(Sets.newSet(), actual);
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testGetEmptyItem() {
        Map<String, AttributeValue> emptyItem = Group.getEmptyItem();
        assertEquals("Group", emptyItem.get("item_type").getS());
    }

    @Test
    public void testGroup() throws Exception {
        Group group = new Group(getGroupTestItem());
        assertEquals("Group", group.itemType);
        assertEquals("GR0001", group.id);
        assertEquals(1, group.marker);
        assertNotNull(group.timeCreated);
        assertEquals("TITLE", group.title);
        assertEquals("DESCRIPTION", group.description);
        assertEquals("MOTTO", group.motto);
        assertEquals("GROUPIMAGEPATH", group.groupImagePath);
        assertEquals(Sets.newSet("1", "2", "3"), group.owners);
        assertEquals(Sets.newSet("1", "2", "3"), group.members);
        assertEquals(Sets.newSet("1", "2", "3"), group.invitedMembers);
        assertEquals(Sets.newSet("1", "2", "3"), group.memberRequests);
        assertEquals(Sets.newSet("1", "2", "3"), group.receivedInvites);
        assertEquals("ACCESS", group.access);
        assertEquals("RESTRICTION", group.restriction);
        assertEquals(Sets.newSet("1", "2", "3"), group.events);
        assertEquals(Sets.newSet("1", "2", "3"), group.completedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), group.challenges);
        assertEquals(Sets.newSet("1", "2", "3"), group.completedChallenges);
        assertEquals(Sets.newSet("1", "2", "3"), group.posts);
        assertEquals(Sets.newSet("1", "2", "3"), group.tags);
        assertEquals(Sets.newSet("1", "2", "3"), group.streaks);
    }

    @Test
    public void testGroupNullDescription() throws Exception {
        assertNull((new Group(getGroupTestItem().with("description", null))).description);
    }

    @Test
    public void testGroupNullMotto() throws Exception {
        assertNull((new Group(getGroupTestItem().with("motto", null))).motto);
    }

    @Test
    public void testGroupNullGroupImagePath() throws Exception {
        assertNull((new Group(getGroupTestItem().with("groupImagePath", null))).groupImagePath);
    }

    @Test
    public void testGroupNullOwners() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("owners", null))).owners);
    }

    @Test
    public void testGroupNullMembers() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("members", null))).members);
    }

    @Test
    public void testGroupNullInvitedMembers() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("invitedMembers", null))).invitedMembers);
    }

    @Test
    public void testGroupNullMemberRequests() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("memberRequests", null))).memberRequests);
    }

    @Test
    public void testGroupNullReceivedInvites() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("receivedInvites", null))).receivedInvites);
    }

    @Test
    public void testGroupNullRestriction() throws Exception {
        assertNull((new Group(getGroupTestItem().with("restriction", null))).restriction);
    }

    @Test
    public void testGroupNullEvents() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("events", null))).events);
    }

    @Test
    public void testGroupNullCompletedEvents() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("completedEvents", null))).completedEvents);
    }

    @Test
    public void testGroupNullChallenges() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("challenges", null))).challenges);
    }

    @Test
    public void testGroupNullCompletedChallenges() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("completedChallenges", null))).completedChallenges);
    }

    @Test
    public void testGroupNullPosts() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("posts", null))).posts);
    }

    @Test
    public void testGroupNullTags() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("tags", null))).tags);
    }

    @Test
    public void testGroupNullStreaks() throws Exception {
        assertEmptySet((new Group(getGroupTestItem().with("streaks", null))).streaks);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failGroupWrongItemTypeAndID() throws Exception {
        new Group(getGroupTestItem().withString("item_type", "Trainer")
                .withString("id", "TR0001"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failGroupNullTitle() throws Exception {
        new Group(getGroupTestItem().with("title", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failGroupNullAccess() throws Exception {
        new Group(getGroupTestItem().with("access", null));
    }
}
