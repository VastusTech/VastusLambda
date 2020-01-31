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
import main.java.databaseObjects.User;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserTest {
    static Item getUserItem() throws Exception {
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
                .withStringSet("streaks", Sets.newSet("1", "2", "3"));
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
        Map<String, AttributeValue> emptyItem = User.getEmptyItem();
        assertEquals("0.0", emptyItem.get("friendlinessRating").getS());
        assertEquals("0.0", emptyItem.get("effectivenessRating").getS());
        assertEquals("0.0", emptyItem.get("reliabilityRating").getS());
    }

    @Test
    public void testUser() throws Exception {
        User user = new User(getUserItem());
        assertEquals("Client", user.itemType);
        assertEquals("CL0001", user.id);
        assertEquals(1, user.marker);
        assertNotNull(user.timeCreated);
        assertEquals("NAME", user.name);
        assertEquals("GENDER", user.gender);
        assertEquals("1998-10-05", user.birthday);
        assertEquals("EMAIL", user.email);
        assertEquals("LOCATION", user.location);
        assertEquals("LB", user.username);
        assertEquals("FEDERATEDID", user.federatedID);
        assertEquals("STRIPEID", user.stripeID);
        assertEquals("PROFILEIMAGEPATH", user.profileImagePath);
        assertEquals(Sets.newSet("1", "2", "3"), user.profileImagePaths);
        assertEquals(Sets.newSet("1", "2", "3"), user.scheduledWorkouts);
        assertEquals(Sets.newSet("1", "2", "3"), user.completedWorkouts);
        assertEquals(Collections.singletonList(getTestTimeInterval()), user.scheduledTimes);
        assertEquals(Sets.newSet("1", "2", "3"), user.reviewsBy);
        assertEquals(Sets.newSet("1", "2", "3"), user.reviewsAbout);
        assertEquals(0.0f, user.friendlinessRating, 0.1f);
        assertEquals(0.0f, user.effectivenessRating, 0.1f);
        assertEquals(0.0f, user.reliabilityRating, 0.1f);
        assertEquals("BIO", user.bio);
        assertEquals(Sets.newSet("1", "2", "3"), user.friends);
        assertEquals(Sets.newSet("1", "2", "3"), user.friendRequests);
        assertEquals(Sets.newSet("1", "2", "3"), user.challenges);
        assertEquals(Sets.newSet("1", "2", "3"), user.ownedChallenges);
        assertEquals(Sets.newSet("1", "2", "3"), user.invitedChallenges);
        assertEquals(Sets.newSet("1", "2", "3"), user.challengesWon);
        assertEquals(Sets.newSet("1", "2", "3"), user.scheduledEvents);
        assertEquals(Sets.newSet("1", "2", "3"), user.completedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), user.ownedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), user.invitedEvents);
        assertEquals(Sets.newSet("1", "2", "3"), user.sentInvites);
        assertEquals(Sets.newSet("1", "2", "3"), user.receivedInvites);
        assertEquals(Sets.newSet("1", "2", "3"), user.posts);
        assertEquals(Sets.newSet("1", "2", "3"), user.submissions);
        assertEquals(Sets.newSet("1", "2", "3"), user.liked);
        assertEquals(Sets.newSet("1", "2", "3"), user.comments);
        assertEquals(Sets.newSet("1", "2", "3"), user.groups);
        assertEquals(Sets.newSet("1", "2", "3"), user.ownedGroups);
        assertEquals(Sets.newSet("1", "2", "3"), user.invitedGroups);
        assertEquals(Sets.newSet("1", "2", "3"), user.messageBoards);
        assertEquals(Sets.newSet("1", "2", "3"), user.streaks);
    }

    @Test
    public void testUserNullGender() throws Exception {
        assertNull((new User(getUserItem().with("gender", null))).gender);
    }

    @Test
    public void testUserNullBirthday() throws Exception {
        assertNull((new User(getUserItem().with("birthday", null))).birthday);
    }

    @Test
    public void testUserNullLocation() throws Exception {
        assertNull((new User(getUserItem().with("location", null))).location);
    }

    @Test
    public void testUserNullFederatedID() throws Exception {
        assertNull((new User(getUserItem().with("federatedID", null))).federatedID);
    }

    @Test
    public void testUserNullStripeID() throws Exception {
        assertNull((new User(getUserItem().with("stripeID", null))).stripeID);
    }

    @Test
    public void testUserNullProfileImagePath() throws Exception {
        assertNull((new User(getUserItem().with("profileImagePath", null))).profileImagePath);
    }

    @Test
    public void testUserNullProfileImagePaths() throws Exception {
        assertEmptySet((new User(getUserItem().with("profileImagePaths", null))).profileImagePaths);
    }

    @Test
    public void testUserNullScheduledWorkouts() throws Exception {
        assertEmptySet((new User(getUserItem().with("scheduledWorkouts", null))).scheduledWorkouts);
    }

    @Test
    public void testUserNullCompletedWorkouts() throws Exception {
        assertEmptySet((new User(getUserItem().with("completedWorkouts", null))).completedWorkouts);
    }

    @Test
    public void testUserNullScheduledTimes() throws Exception {
        assertEquals(new ArrayList<>(), (new User(getUserItem().with("scheduledTimes", null))).scheduledTimes);
    }

    @Test
    public void testUserNullReviewsBy() throws Exception {
        assertEmptySet((new User(getUserItem().with("reviewsBy", null))).reviewsBy);
    }

    @Test
    public void testUserNullReviewsAbout() throws Exception {
        assertEmptySet((new User(getUserItem().with("reviewsAbout", null))).reviewsAbout);
    }

    @Test
    public void testUserNullBio() throws Exception {
        assertNull((new User(getUserItem().with("bio", null))).bio);
    }

    @Test
    public void testUserNullFriends() throws Exception {
        assertEmptySet((new User(getUserItem().with("friends", null))).friends);
    }

    @Test
    public void testUserNullFriendRequests() throws Exception {
        assertEmptySet((new User(getUserItem().with("friendRequests", null))).friendRequests);
    }

    @Test
    public void testUserNullChallenges() throws Exception {
        assertEmptySet((new User(getUserItem().with("challenges", null))).challenges);
    }

    @Test
    public void testUserNullOwnedChallenges() throws Exception {
        assertEmptySet((new User(getUserItem().with("ownedChallenges", null))).ownedChallenges);
    }

    @Test
    public void testUserNullInvitedChallenges() throws Exception {
        assertEmptySet((new User(getUserItem().with("invitedChallenges", null))).invitedChallenges);
    }

    @Test
    public void testUserNullChallengesWon() throws Exception {
        assertEmptySet((new User(getUserItem().with("challengesWon", null))).challengesWon);
    }

    @Test
    public void testUserNullScheduledEvents() throws Exception {
        assertEmptySet((new User(getUserItem().with("scheduledEvents", null))).scheduledEvents);
    }

    @Test
    public void testUserNullCompletedEvents() throws Exception {
        assertEmptySet((new User(getUserItem().with("completedEvents", null))).completedEvents);
    }

    @Test
    public void testUserNullInvitedEvents() throws Exception {
        assertEmptySet((new User(getUserItem().with("invitedEvents", null))).invitedEvents);
    }

    @Test
    public void testUserNullSentInvites() throws Exception {
        assertEmptySet((new User(getUserItem().with("sentInvites", null))).sentInvites);
    }

    @Test
    public void testUserNullReceivedInvites() throws Exception {
        assertEmptySet((new User(getUserItem().with("receivedInvites", null))).receivedInvites);
    }

    @Test
    public void testUserNullPosts() throws Exception {
        assertEmptySet((new User(getUserItem().with("posts", null))).posts);
    }

    @Test
    public void testUserNullSubmissions() throws Exception {
        assertEmptySet((new User(getUserItem().with("submissions", null))).submissions);
    }

    @Test
    public void testUserNullLiked() throws Exception {
        assertEmptySet((new User(getUserItem().with("liked", null))).liked);
    }

    @Test
    public void testUserNullComments() throws Exception {
        assertEmptySet((new User(getUserItem().with("comments", null))).comments);
    }

    @Test
    public void testUserNullGroups() throws Exception {
        assertEmptySet((new User(getUserItem().with("groups", null))).groups);
    }

    @Test
    public void testUserNullOwnedGroups() throws Exception {
        assertEmptySet((new User(getUserItem().with("ownedGroups", null))).ownedGroups);
    }

    @Test
    public void testUserNullInvitedGroups() throws Exception {
        assertEmptySet((new User(getUserItem().with("invitedGroups", null))).invitedGroups);
    }

    @Test
    public void testUserNullMessageBoards() throws Exception {
        assertEmptySet((new User(getUserItem().with("messageBoards", null))).messageBoards);
    }

    @Test
    public void testUserNullStreaks() throws Exception {
        assertEmptySet((new User(getUserItem().with("streaks", null))).streaks);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failNullName() throws Exception {
        new User(getUserItem().with("name", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullEmail() throws Exception {
        new User(getUserItem().with("email", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullUsername() throws Exception {
        new User(getUserItem().with("username", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullFriendlinessRating() throws Exception {
        new User(getUserItem().with("friendlinessRating", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failBadFriendlinessRating() throws Exception {
        new User(getUserItem().withString("friendlinessRating", "BAD"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullEffectivenessRating() throws Exception {
        new User(getUserItem().with("effectivenessRating", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failBadEffectivenessRating() throws Exception {
        new User(getUserItem().withString("effectivenessRating", "BAD"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failNullReliabilityRating() throws Exception {
        new User(getUserItem().with("reliabilityRating", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failBadReliabilityRating() throws Exception {
        new User(getUserItem().withString("reliabilityRating", "BAD"));
    }
}
