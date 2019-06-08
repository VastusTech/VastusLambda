package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import main.java.databaseObjects.Sponsor;
import main.java.databaseObjects.TimeInterval;
import main.java.logic.TimeHelper;

// TODO REVISIT ONCE IMPLEMENTED BETTER
public class SponsorTest {
    static Item getSponsorTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Sponsor")
                .withString("id", "SP0001")
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

    @Test
    public void test() {
        Sponsor sponsor;
    }
}
