package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Map;

import main.java.databaseObjects.Challenge;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ChallengeTest {
    static Item getChallengeTestItem() {
        return new Item()
                .withString("item_type", "Challenge")
                .withString("id", "CH0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("title", "TITLE")
                .withString("description", "DESCRIPTION")
                .withString("owner", "OWNER")
                .withString("endTime", TimeHelper.isoString(TimeHelper.hoursFromNow(24)))
                .withStringSet("members", Sets.newSet("1", "2", "3"))
                .with("invitedMembers", null)
                .with("memberRequests", null)
                .with("receivedInvites", null)
                .withString("capacity", "100")
                .withString("ifCompleted", "false")
                .withString("access", "private")
                .with("restriction", null)
                .with("events", null)
                .with("completedEvents", null)
                .with("group", null)
                .withString("goal", "GOAL")
                .with("challengeType", null)
                .withString("difficulty", "2")
                .with("winner", null)
                .withString("prize", "PRIZE")
                .with("tags", null)
                .with("submissions", null)
                .with("streaks", null)
                .with("streakUpdateSpanType", null)
                .with("streakUpdateInterval", null)
                .with("streakN", null);
    }


    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testGetEmptyItem() {
        Map<String, AttributeValue> emptyItem = Challenge.getEmptyItem();
        assertEquals("Challenge", emptyItem.get("item_type").getS());
        assertEquals("10", emptyItem.get("capacity").getS());
        assertEquals("0", emptyItem.get("difficulty").getS());
        assertEquals("false", emptyItem.get("ifCompleted").getS());
    }

    @Test
    public void testChallenge() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem());
        assertEquals("Challenge", challenge.itemType);
        assertEquals("CH0001", challenge.id);
        assertEquals(1, challenge.marker);
        assertNotNull(challenge.timeCreated);
        assertEquals("TITLE", challenge.title);
        assertEquals("DESCRIPTION", challenge.description);
        assertEquals("OWNER", challenge.owner);
        assertNotNull(challenge.endTime);
        assertEquals(Sets.newSet("1", "2", "3"), challenge.members);
        assertEquals(Sets.newSet(), challenge.invitedMembers);
        assertEquals(Sets.newSet(), challenge.memberRequests);
        assertEquals(Sets.newSet(), challenge.receivedInvites);
        assertEquals(100, challenge.capacity);
        assertFalse(challenge.ifCompleted);
        assertEquals("private", challenge.access);
        assertNull(challenge.restriction);
        assertEquals(Sets.newSet(), challenge.events);
        assertEquals(Sets.newSet(), challenge.completedEvents);
        assertNull(challenge.group);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertEquals(2, challenge.difficulty);
        assertNull(challenge.winner);
        assertEquals("PRIZE", challenge.prize);
        assertEquals(Sets.newSet(), challenge.tags);
        assertEquals(Sets.newSet(), challenge.submissions);
        assertEquals(Sets.newSet(), challenge.streaks);
        assertNull(challenge.streakUpdateSpanType);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakN);
    }

    @Test
    public void testChallengeWithNullMembers() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("members", null));
        assertEquals(Sets.newSet(), challenge.members);
    }

    @Test
    public void testChallengeWithNullDescription() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("description", null));
        assertNull(challenge.description);
    }

    @Test
    public void testChallengeWithInvitedMembers() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("invitedMembers", null));
        assertEquals(Sets.newSet(), challenge.invitedMembers);
    }

    @Test
    public void testChallengeWithMemberRequests() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("memberRequests", null));
        assertEquals(Sets.newSet(), challenge.memberRequests);
    }

    @Test
    public void testChallengeWithReceivedInvites() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("receivedInvites", null));
        assertEquals(Sets.newSet(), challenge.receivedInvites);
    }

    @Test
    public void testChallengeWithTrueCompletion() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().withString("ifCompleted", "true"));
        assertTrue(challenge.ifCompleted);
    }

    @Test
    public void testChallengeWithRestriction() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().withString("restriction", "invite"));
        assertEquals("invite", challenge.restriction);
    }

    @Test
    public void testChallengeWithEvents() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("events", null));
        assertEquals(Sets.newSet(), challenge.events);
    }

    @Test
    public void testChallengeWithCompletedEvents() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("completedEvents", null));
        assertEquals(Sets.newSet(), challenge.completedEvents);
    }

    @Test
    public void testChallengeWithGroup() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("group", "GR0001"));
        assertEquals("GR0001", challenge.group);
    }

    @Test
    public void testChallengeWithChallengeType() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("challengeType", "streak"));
        assertEquals("streak", challenge.challengeType);
    }

    @Test
    public void testChallengeWithWinner() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("winner", "CL0001"));
        assertEquals("CL0001", challenge.winner);
    }

    @Test
    public void testChallengeWithNullPrize() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("prize", null));
        assertNull(challenge.prize);
    }

    @Test
    public void testChallengeWithTags() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("tags", null));
        assertEquals(Sets.newSet(), challenge.tags);
    }

    @Test
    public void testChallengeWithSubmissions() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("submissions", null));
        assertEquals(Sets.newSet(), challenge.submissions);
    }

    @Test
    public void testChallengeWithStreaks() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("streaks", null));
        assertEquals(Sets.newSet(), challenge.streaks);
    }

    @Test
    public void testChallengeWithStreakUpdateSpanType() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("streakUpdateSpanType", "daily"));
        assertEquals("daily", challenge.streakUpdateSpanType);
    }

    @Test
    public void testChallengeWithStreakUpdateInterval() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("streakUpdateInterval", "1"));
        assertEquals("1", challenge.streakUpdateInterval);
    }

    @Test
    public void testChallengeWithStreakN() throws Exception {
        Challenge challenge = new Challenge(getChallengeTestItem().with("streakN", "1"));
        assertEquals("1", challenge.streakN);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failNullTitle() throws Exception {
        new Challenge(getChallengeTestItem().with("title", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullOwner() throws Exception {
        new Challenge(getChallengeTestItem().with("owner", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullCapacity() throws Exception {
        new Challenge(getChallengeTestItem().with("capacity", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failBadCapacity() throws Exception {
        new Challenge(getChallengeTestItem().withString("capacity", "not-a-good-capacity"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullGoal() throws Exception {
        new Challenge(getChallengeTestItem().with("goal", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullEndTime() throws Exception {
        new Challenge(getChallengeTestItem().with("endTime", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullAccess() throws Exception {
        new Challenge(getChallengeTestItem().with("access", null));
    }
}
