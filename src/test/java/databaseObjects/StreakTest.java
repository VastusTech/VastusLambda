package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Map;

import main.java.databaseObjects.Streak;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StreakTest {
    static Item getStreakTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Streak")
                .withString("id", "ST0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("owner", "OWNER")
                .withString("about", "ABOUT")
                .withNumber("N", 1)
                .withNumber("bestN", 1)
                .withNumber("currentN", 1)
                .withString("lastUpdated", TimeHelper.nowString())
                .withString("lastAttemptStarted", TimeHelper.nowString())
                .withString("streakType", "STREAKTYPE")
                .withString("updateSpanType", "daily")
                .withString("updateInterval", "2")
                .withString("streakN", "1") ;
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testGetEmptyItem() {
        Map<String, AttributeValue> emptyItem = Streak.getEmptyItem();
        assertEquals("Streak", emptyItem.get("item_type").getS());
        assertEquals("0", emptyItem.get("N").getN());
        assertEquals("0", emptyItem.get("bestN").getN());
        assertEquals("0", emptyItem.get("currentN").getN());
        assertNotNull(emptyItem.get("lastUpdated").getS());
        assertNotNull(emptyItem.get("lastAttemptStarted").getS());
    }

    @Test
    public void testStreak() throws Exception {
        Streak streak = new Streak(getStreakTestItem());
        assertEquals("Streak", streak.itemType);
        assertEquals("ST0001", streak.id);
        assertEquals(1, streak.marker);
        assertNotNull(streak.timeCreated);
        assertEquals("OWNER", streak.owner);
        assertEquals("ABOUT", streak.about);
        assertEquals(1, streak.N);
        assertEquals(1, streak.bestN);
        assertEquals(1, streak.currentN);
        assertNotNull(streak.lastUpdated);
        assertNotNull(streak.lastAttemptStarted);
        assertEquals("STREAKTYPE", streak.streakType);
        assertEquals(Streak.UpdateSpanType.daily, streak.updateSpanType);
        assertEquals(2, streak.updateInterval);
        assertEquals(1, streak.streakN);
    }

    @Test
    public void testStreakNullStreakType() throws Exception {
        assertNull((new Streak(getStreakTestItem().with("streakType", null))).streakType);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failStreakWrongItemTypeAndID() throws Exception {
        new Streak(getStreakTestItem().withString("item_type", "Trainer")
                .withString("id", "TR0001"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullOwner() throws Exception {
        new Streak(getStreakTestItem().with("owner", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullAbout() throws Exception {
        new Streak(getStreakTestItem().with("about", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullN() throws Exception {
        new Streak(getStreakTestItem().with("N", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakLowN() throws Exception {
        new Streak(getStreakTestItem().withNumber("N", -1));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullBestN() throws Exception {
        new Streak(getStreakTestItem().with("bestN", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakLowBestN() throws Exception {
        new Streak(getStreakTestItem().withNumber("bestN", -1));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullCurrentN() throws Exception {
        new Streak(getStreakTestItem().with("currentN", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakLowCurrentN() throws Exception {
        new Streak(getStreakTestItem().withNumber("currentN", -1));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullLastUpdated() throws Exception {
        new Streak(getStreakTestItem().with("lastUpdated", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullLastAttemptStarted() throws Exception {
        new Streak(getStreakTestItem().with("lastAttemptStarted", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullUpdateSpanType() throws Exception {
        new Streak(getStreakTestItem().with("updateSpanType", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakBadUpdateSpanType() throws Exception {
        new Streak(getStreakTestItem().withString("updateSpanType", "not-a-update-span-type"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullUpdateInterval() throws Exception {
        new Streak(getStreakTestItem().with("updateInterval", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakLowUpdateInterval() throws Exception {
        new Streak(getStreakTestItem().withNumber("updateInterval", -1));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakNullStreakN() throws Exception {
        new Streak(getStreakTestItem().with("streakN", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failStreakLowStreakN() throws Exception {
        new Streak(getStreakTestItem().withNumber("streakN", -1));
    }
}
