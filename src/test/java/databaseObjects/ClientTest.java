package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Collections;
import java.util.Map;

import main.java.databaseObjects.Client;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ClientTest {
    static Item getClientTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Client")
                .withString("id", "CL0001")
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
                .withStringSet("trainersFollowing", Sets.newSet("1", "2", "3"))
                .withStringSet("subscriptions", Sets.newSet("1", "2", "3"));
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
        Map<String, AttributeValue> emptyItem = Client.getEmptyItem();
        assertEquals("Client", emptyItem.get("item_type").getS());
    }

    @Test
    public void testClient() throws Exception {
        Client client = new Client(getClientTestItem());
        assertEquals("Client", client.itemType);
        assertEquals("CL0001", client.id);
        assertEquals(1, client.marker);
        assertNotNull(client.timeCreated);
        assertEquals("NAME", client.name);
        assertEquals("GENDER", client.gender);
        assertEquals("1998-10-05", client.birthday);
        assertEquals("EMAIL", client.email);
        assertEquals("LOCATION", client.location);
        assertEquals("LB", client.username);
        assertEquals("FEDERATEDID", client.federatedID);
        assertEquals("STRIPEID", client.stripeID);
        assertEquals("PROFILEIMAGEPATH", client.profileImagePath);
        assertEquals(Sets.newSet("1", "2", "3"), client.profileImagePaths);
        assertEquals(Sets.newSet("1", "2", "3"), client.scheduledWorkouts);
        assertEquals(Sets.newSet("1", "2", "3"), client.completedWorkouts);
        assertEquals(Collections.singletonList(getTestTimeInterval()), client.scheduledTimes);
        assertEquals(Sets.newSet("1", "2", "3"), client.reviewsBy);
        assertEquals(Sets.newSet("1", "2", "3"), client.reviewsAbout);
        assertEquals(0.0f, client.friendlinessRating, 0.1f);
        assertEquals(0.0f, client.effectivenessRating, 0.1f);
        assertEquals(0.0f, client.reliabilityRating, 0.1f);
        assertEquals("BIO", client.bio);
        assertEquals(Sets.newSet("1", "2", "3"), client.friends);
        assertEquals(Sets.newSet("1", "2", "3"), client.friendRequests);
        assertEquals(Sets.newSet("1", "2", "3"), client.challenges);
        assertEquals(Sets.newSet("1", "2", "3"), client.ownedChallenges);
        assertEquals(Sets.newSet("1", "2", "3"), client.invitedChallenges);
        assertEquals(Sets.newSet("1", "2", "3"), client.challengesWon);
        assertEquals(Sets.newSet("1", "2", "3"), client.scheduledEvents);
        assertEquals(Sets.newSet("1", "2", "3"), client.completedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), client.ownedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), client.invitedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), client.sentInvites);
        assertEquals(Sets.newSet("1", "2", "3"), client.receivedInvites);
        assertEquals(Sets.newSet("1", "2", "3"), client.posts);
        assertEquals(Sets.newSet("1", "2", "3"), client.submissions);
        assertEquals(Sets.newSet("1", "2", "3"), client.liked);
        assertEquals(Sets.newSet("1", "2", "3"), client.comments);
        assertEquals(Sets.newSet("1", "2", "3"), client.groups);
        assertEquals(Sets.newSet("1", "2", "3"), client.ownedGroups);
        assertEquals(Sets.newSet("1", "2", "3"), client.invitedGroups);
        assertEquals(Sets.newSet("1", "2", "3"), client.messageBoards);
        assertEquals(Sets.newSet("1", "2", "3"), client.streaks);
        assertEquals(Sets.newSet("1", "2", "3"), client.trainersFollowing);
        assertEquals(Sets.newSet("1", "2", "3"), client.subscriptions);
    }

    @Test
    public void testClientNullTrainersFollowing() throws Exception {
        assertEmptySet((new Client(getClientTestItem().with("trainersFollowing", null))).trainersFollowing);
    }

    @Test
    public void testClientNullSubscriptions() throws Exception {
        assertEmptySet((new Client(getClientTestItem().with("subscriptions", null))).subscriptions);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failClientWrongItemTypeAndID() throws Exception {
        new Client(getClientTestItem().withString("item_type", "Trainer")
                .withString("id", "TR0001"));
    }
}
