package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import main.java.databaseObjects.TimeInterval;
import main.java.databaseObjects.Trainer;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TrainerTest {
    static Item getTrainerTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Trainer")
                .withString("id", "TR0001")
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
                .withString("gym", "GYM")
                .withStringSet("availableTimes", getTestTimeInterval().toString())
                .withString("workoutSticker", "STICKER")
                .withString("preferredIntensity", "3")
                .withString("workoutCapacity", "4")
                .withString("workoutPrice", "80")
                .withStringSet("followers", Sets.newSet("1", "2", "3"))
                .withStringSet("subscribers", Sets.newSet("1", "2", "3"))
                .withString("subscriptionPrice", "10")
                .withStringSet("certifications", Sets.newSet("1", "2", "3")) ;
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
        Map<String, AttributeValue> emptyItem = Trainer.getEmptyItem();
        assertEquals("Trainer", emptyItem.get("item_type").getS());
    }

    @Test
    public void testTrainer() throws Exception {
        Trainer trainer = new Trainer(getTrainerTestItem());
        assertEquals("Trainer", trainer.itemType);
        assertEquals("TR0001", trainer.id);
        assertEquals(1, trainer.marker);
        assertNotNull(trainer.timeCreated);
        assertEquals("NAME", trainer.name);
        assertEquals("GENDER", trainer.gender);
        assertEquals("1998-10-05", trainer.birthday);
        assertEquals("EMAIL", trainer.email);
        assertEquals("LOCATION", trainer.location);
        assertEquals("LB", trainer.username);
        assertEquals("FEDERATEDID", trainer.federatedID);
        assertEquals("STRIPEID", trainer.stripeID);
        assertEquals("PROFILEIMAGEPATH", trainer.profileImagePath);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.profileImagePaths);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.scheduledWorkouts);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.completedWorkouts);
        assertEquals(Collections.singletonList(getTestTimeInterval()), trainer.scheduledTimes);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.reviewsBy);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.reviewsAbout);
        assertEquals(0.0f, trainer.friendlinessRating, 0.1f);
        assertEquals(0.0f, trainer.effectivenessRating, 0.1f);
        assertEquals(0.0f, trainer.reliabilityRating, 0.1f);
        assertEquals("BIO", trainer.bio);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.friends);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.friendRequests);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.challenges);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.ownedChallenges);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.invitedChallenges);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.challengesWon);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.scheduledEvents);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.completedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.ownedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.invitedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.sentInvites);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.receivedInvites);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.posts);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.submissions);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.liked);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.comments);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.groups);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.ownedGroups);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.invitedGroups);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.messageBoards);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.streaks);
        assertEquals("GYM", trainer.gym);
        assertEquals(Collections.singletonList(getTestTimeInterval()), trainer.availableTimes);
        assertEquals("STICKER", trainer.workoutSticker);
        assertEquals("3", trainer.preferredIntensity);
        assertEquals(4, trainer.workoutCapacity);
        assertEquals(80, trainer.workoutPrice);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.followers);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.subscribers);
        assertEquals("10", trainer.subscriptionPrice);
        assertEquals(Sets.newSet("1", "2", "3"), trainer.certifications);
    }

    @Test
    public void testTrainerNullGym() throws Exception {
        assertNull((new Trainer(getTrainerTestItem().with("gym", null))).gym);
    }

    @Test
    public void testTrainerNullAvailableTimes() throws Exception {
        assertEquals(new ArrayList<>(), (new Trainer(getTrainerTestItem().with("availableTimes", null))).availableTimes);
    }

    @Test
    public void testTrainerNullWorkoutSticker() throws Exception {
        assertNull((new Trainer(getTrainerTestItem().with("workoutSticker", null))).workoutSticker);
    }

    @Test
    public void testTrainerNullPreferredIntensity() throws Exception {
        assertNull((new Trainer(getTrainerTestItem().with("preferredIntensity", null))).preferredIntensity);
    }

    @Test
    public void testTrainerNullWorkoutCapacity() throws Exception {
        assertEquals(-1, (new Trainer(getTrainerTestItem().with("workoutCapacity", null))).workoutCapacity);
    }

    @Test
    public void testTrainerNullWorkoutPrice() throws Exception {
        assertEquals(-1, (new Trainer(getTrainerTestItem().with("workoutPrice", null))).workoutPrice);
    }

    @Test
    public void testTrainerNullFollowers() throws Exception {
        assertEmptySet((new Trainer(getTrainerTestItem().with("followers", null))).followers);
    }

    @Test
    public void testTrainerNullSubscribers() throws Exception {
        assertEmptySet((new Trainer(getTrainerTestItem().with("subscribers", null))).subscribers);
    }

    @Test
    public void testTrainerNullSubscriptionPrice() throws Exception {
        assertNull((new Trainer(getTrainerTestItem().with("subscriptionPrice", null))).subscriptionPrice);
    }

    @Test
    public void testTrainerNullCertifications() throws Exception {
        assertEmptySet((new Trainer(getTrainerTestItem().with("certifications", null))).certifications);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failTrainerWrongItemTypeAndID() throws Exception {
        new Trainer(getTrainerTestItem().withString("item_type", "Client")
                .withString("id", "CL0001"));
    }
}
