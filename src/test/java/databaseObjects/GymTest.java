package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import main.java.databaseObjects.Gym;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class GymTest {
    static Item getGymTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Gym")
                .withString("id", "GY0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("name", "NAME")
                .withString("gender", "GENDER")
                .withString("birthday", "1998-10-05")
                .withString("email", "EMAIL")
                .withString("location", "LOCATION")
                .withString("username", "LB")
                .withString("federatedID", "FEDERATEDID")
                .withString("stripeID", "STRIPEID")
                .withString("profileImagePath", "PROFILEIMAGEPATH")
                .withStringSet("profileImagePaths", Sets.newSet("1", "2", "3"))
                .withStringSet("scheduledWorkouts", Sets.newSet("1", "2", "3"))
                .withStringSet("completedWorkouts", Sets.newSet("1", "2", "3"))
                .withStringSet("scheduledTimes", getTestTimeInterval().toString())
                .withStringSet("reviewsBy", Sets.newSet("1", "2", "3"))
                .withStringSet("reviewsAbout", Sets.newSet("1", "2", "3"))
                .withString("friendlinessRating", "0.0")
                .withString("effectivenessRating", "0.0")
                .withString("reliabilityRating", "0.0")
                .withString("bio", "BIO")
                .withStringSet("friends", Sets.newSet("1", "2", "3"))
                .withStringSet("friendRequests", Sets.newSet("1", "2", "3"))
                .withStringSet("challenges", Sets.newSet("1", "2", "3"))
                .withStringSet("ownedChallenges", Sets.newSet("1", "2", "3"))
                .withStringSet("invitedChallenges", Sets.newSet("1", "2", "3"))
                .withStringSet("challengesWon", Sets.newSet("1", "2", "3"))
                .withStringSet("scheduledEvents", Sets.newSet("1", "2", "3"))
                .withStringSet("completedEvents", Sets.newSet("1", "2", "3"))
                .withStringSet("ownedEvents", Sets.newSet("1", "2", "3"))
                .withStringSet("invitedEvents", Sets.newSet("1", "2", "3"))
                .withStringSet("sentInvites", Sets.newSet("1", "2", "3"))
                .withStringSet("receivedInvites", Sets.newSet("1", "2", "3"))
                .withStringSet("posts", Sets.newSet("1", "2", "3"))
                .withStringSet("submissions", Sets.newSet("1", "2", "3"))
                .withStringSet("liked", Sets.newSet("1", "2", "3"))
                .withStringSet("comments", Sets.newSet("1", "2", "3"))
                .withStringSet("groups", Sets.newSet("1", "2", "3"))
                .withStringSet("ownedGroups", Sets.newSet("1", "2", "3"))
                .withStringSet("invitedGroups", Sets.newSet("1", "2", "3"))
                .withStringSet("messageBoards", Sets.newSet("1", "2", "3"))
                .withStringSet("streaks", Sets.newSet("1", "2", "3"))
                .withString("address", "ADDRESS")
                .withStringSet("trainers", Sets.newSet("1", "2", "3"))
                .withStringSet("weeklyHours", getTestTimeInterval().toString())
                .withStringSet("vacationTimes", getTestTimeInterval().toString())
                .withString("sessionCapacity", "10")
                .withString("gymType", "GYMTYPE")
                .withString("paymentSplit", "50") ;
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
        Map<String, AttributeValue> emptyItem = Gym.getEmptyItem();
        assertEquals("Gym", emptyItem.get("item_type").getS());
        assertEquals("10", emptyItem.get("sessionCapacity").getS());
        assertEquals("independent", emptyItem.get("gymType").getS());
        assertEquals("50", emptyItem.get("paymentSplit").getS());
    }

    @Test
    public void testGym() throws Exception {
        Gym gym = new Gym(getGymTestItem());
        assertEquals("Gym", gym.itemType);
        assertEquals("GY0001", gym.id);
        assertEquals(1, gym.marker);
        assertNotNull(gym.timeCreated);
        assertEquals("NAME", gym.name);
        assertEquals("GENDER", gym.gender);
        assertEquals("1998-10-05", gym.birthday);
        assertEquals("EMAIL", gym.email);
        assertEquals("LOCATION", gym.location);
        assertEquals("LB", gym.username);
        assertEquals("FEDERATEDID", gym.federatedID);
        assertEquals("STRIPEID", gym.stripeID);
        assertEquals("PROFILEIMAGEPATH", gym.profileImagePath);
        assertEquals(Sets.newSet("1", "2", "3"), gym.profileImagePaths);
        assertEquals(Sets.newSet("1", "2", "3"), gym.scheduledWorkouts);
        assertEquals(Sets.newSet("1", "2", "3"), gym.completedWorkouts);
        assertEquals(Collections.singletonList(getTestTimeInterval()), gym.scheduledTimes);
        assertEquals(Sets.newSet("1", "2", "3"), gym.reviewsBy);
        assertEquals(Sets.newSet("1", "2", "3"), gym.reviewsAbout);
        assertEquals(0.0f, gym.friendlinessRating, 0.1f);
        assertEquals(0.0f, gym.effectivenessRating, 0.1f);
        assertEquals(0.0f, gym.reliabilityRating, 0.1f);
        assertEquals("BIO", gym.bio);
        assertEquals(Sets.newSet("1", "2", "3"), gym.friends);
        assertEquals(Sets.newSet("1", "2", "3"), gym.friendRequests);
        assertEquals(Sets.newSet("1", "2", "3"), gym.challenges);
        assertEquals(Sets.newSet("1", "2", "3"), gym.ownedChallenges);
        assertEquals(Sets.newSet("1", "2", "3"), gym.invitedChallenges);
        assertEquals(Sets.newSet("1", "2", "3"), gym.challengesWon);
        assertEquals(Sets.newSet("1", "2", "3"), gym.scheduledEvents);
        assertEquals(Sets.newSet("1", "2", "3"), gym.completedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), gym.ownedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), gym.invitedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), gym.sentInvites);
        assertEquals(Sets.newSet("1", "2", "3"), gym.receivedInvites);
        assertEquals(Sets.newSet("1", "2", "3"), gym.posts);
        assertEquals(Sets.newSet("1", "2", "3"), gym.submissions);
        assertEquals(Sets.newSet("1", "2", "3"), gym.liked);
        assertEquals(Sets.newSet("1", "2", "3"), gym.comments);
        assertEquals(Sets.newSet("1", "2", "3"), gym.groups);
        assertEquals(Sets.newSet("1", "2", "3"), gym.ownedGroups);
        assertEquals(Sets.newSet("1", "2", "3"), gym.invitedGroups);
        assertEquals(Sets.newSet("1", "2", "3"), gym.messageBoards);
        assertEquals(Sets.newSet("1", "2", "3"), gym.streaks);
        assertEquals("ADDRESS", gym.address);
        assertEquals(Sets.newSet("1", "2", "3"), gym.trainers);
        assertEquals(Collections.singletonList(getTestTimeInterval()), gym.weeklyHours);
        assertEquals(Collections.singletonList(getTestTimeInterval()), gym.vacationTimes);
        assertEquals(10, gym.sessionCapacity);
        assertEquals("GYMTYPE", gym.gymType);
        assertEquals(50.0f, gym.paymentSplit, 0.1f);
    }

    @Test
    public void testGymNullAddress() throws Exception {
        assertNull((new Gym(getGymTestItem().with("address", null)).address));
    }

    @Test
    public void testGymNullTrainers() throws Exception {
        assertEmptySet((new Gym(getGymTestItem().with("trainers", null)).trainers));
    }

    @Test
    public void testGymNullWeeklyHours() throws Exception {
        assertEquals(new ArrayList<>(), (new Gym(getGymTestItem().with("weeklyHours", null)).weeklyHours));
    }

    @Test
    public void testGymNullVacationTimes() throws Exception {
        assertEquals(new ArrayList<>(), (new Gym(getGymTestItem().with("vacationTimes", null)).vacationTimes));
    }

    @Test
    public void testGymNullSessionCapacity() throws Exception {
        assertEquals(-1, (new Gym(getGymTestItem().with("sessionCapacity", null)).sessionCapacity));
    }

    @Test
    public void testGymNullGymType() throws Exception {
        assertNull((new Gym(getGymTestItem().with("gymType", null)).gymType));
    }

    @Test
    public void testGymNullPaymentSplit() throws Exception {
        assertEquals(-1, new Gym(getGymTestItem().with("paymentSplit", null)).paymentSplit, 0.1f);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failGymWrongItemTypeAndID() throws Exception {
        new Gym(getGymTestItem().withString("item_type", "Client")
                .withString("id", "CL0001"));
    }
}
