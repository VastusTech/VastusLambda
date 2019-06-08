package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Map;

import main.java.databaseObjects.Invite;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InviteTest {
    static Item getInviteTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Invite")
                .withString("id", "IN0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("from", "FROM")
                .withString("to", "TO")
                .withString("inviteType", "friendRequest")
                .withString("about", "ABOUT")
                .withString("description", "DESCRIPTION");
    }

    private void assertEmptySet(Object actual) {
        assertEquals(Sets.newSet(), actual);
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testGetEmptyItem() {
        Map<String, AttributeValue> emptyItem = Invite.getEmptyItem();
        assertEquals("Invite", emptyItem.get("item_type").getS());
    }

    @Test
    public void testInvite() throws Exception {
        Invite invite = new Invite(getInviteTestItem());
        assertEquals("Invite", invite.itemType);
        assertEquals("IN0001", invite.id);
        assertEquals(1, invite.marker);
        assertNotNull(invite.timeCreated);
        assertEquals("FROM", invite.from);
        assertEquals("TO", invite.to);
        assertEquals(Invite.InviteType.friendRequest, invite.inviteType);
        assertEquals("ABOUT", invite.about);
        assertEquals("DESCRIPTION", invite.description);
    }

    @Test
    public void testInviteNulLDescription() throws Exception {
        assertNull((new Invite(getInviteTestItem().with("description", null))).description);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failInviteWrongItemTypeAndID() throws Exception {
        new Invite(getInviteTestItem().withString("item_type", "Trainer")
                .withString("id", "TR0001"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failInviteNullFrom() throws Exception {
        new Invite(getInviteTestItem().with("from", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failInviteNullTo() throws Exception {
        new Invite(getInviteTestItem().with("to", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failInviteNullInviteType() throws Exception {
        new Invite(getInviteTestItem().with("inviteType", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failInviteBadInviteType() throws Exception {
        new Invite(getInviteTestItem().with("inviteType", "not-an-invite-type"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failInviteNullAbout() throws Exception {
        new Invite(getInviteTestItem().with("about", null));
    }
}
