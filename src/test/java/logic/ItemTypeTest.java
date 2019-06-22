package test.java.logic;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import main.java.databaseOperations.exceptions.BadIDException;
import main.java.logic.Constants;
import main.java.logic.ItemType;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemTypeTest {
    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testNoOverlappingPrefixes() throws AssertionError {
        Set<String> prefixes = new HashSet<>();
        for (ItemType itemType : ItemType.values()) {
            String typeName = itemType.name();
            String prefix = typeName.toUpperCase().substring(0, Constants.numPrefix);
            if (prefixes.contains(prefix)) {
                throw new AssertionError("Item type prefix overlaps with another item type! Item Type: " + typeName);
            }
            prefixes.add(prefix);
        }
    }

    @Test
    public void testGetItemTypeFromID() throws BadIDException {
        assertEquals("Client", ItemType.getItemType("CL0001"));
        assertEquals("Trainer", ItemType.getItemType("TR0001"));
        assertEquals("Gym", ItemType.getItemType("GY0001"));
        assertEquals("Workout", ItemType.getItemType("WO0001"));
        assertEquals("Review", ItemType.getItemType("RE0001"));
        assertEquals("Event", ItemType.getItemType("EV0001"));
        assertEquals("Challenge", ItemType.getItemType("CH0001"));
        assertEquals("Invite", ItemType.getItemType("IN0001"));
        assertEquals("Post", ItemType.getItemType("PO0001"));
        assertEquals("Submission", ItemType.getItemType("SU0001"));
        assertEquals("Group", ItemType.getItemType("GR0001"));
        assertEquals("Comment", ItemType.getItemType("CO0001"));
        assertEquals("Sponsor", ItemType.getItemType("SP0001"));
        assertEquals("Message", ItemType.getItemType("ME0001"));
        assertEquals("Streak", ItemType.getItemType("ST0001"));
        assertEquals("Enterprise", ItemType.getItemType("EN0001"));
        assertEquals("Deal", ItemType.getItemType("DE0001"));
    }

    @Test
    public void testIfItemType() {
        assertTrue(ItemType.ifItemType("Message"));
        assertFalse(ItemType.ifItemType("NotAMessage"));
        assertTrue(ItemType.ifItemType("Streak"));
        assertFalse(ItemType.ifItemType("SuperNotAStreak"));
        assertTrue(ItemType.ifItemType("Client"));
        assertFalse(ItemType.ifItemType(""));
        assertFalse(ItemType.ifItemType(null));
        assertFalse(ItemType.ifItemType("GodHasComeToReapTheSinnersOfThisProgram"));
        assertFalse(ItemType.ifItemType("Bubble"));
    }


    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = BadIDException.class)
    public void failGetNullItemType() throws BadIDException {
        ItemType.getItemType(null);
    }

    @Test(expected = BadIDException.class)
    public void failGetEmptyItemType() throws BadIDException {
        ItemType.getItemType("");
    }

    @Test(expected = BadIDException.class)
    public void failGetShortItemType() throws BadIDException {
        ItemType.getItemType("C");
    }

    @Test(expected = BadIDException.class)
    public void failGetInvalidItemType() throws BadIDException {
        ItemType.getItemType("AV0001");
    }
}
