package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import org.junit.Test;

import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Client;
import main.java.databaseObjects.Comment;
import main.java.databaseObjects.DatabaseItemFactory;
import main.java.databaseObjects.Enterprise;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.Gym;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.Message;
import main.java.databaseObjects.Post;
import main.java.databaseObjects.Review;
import main.java.databaseObjects.Sponsor;
import main.java.databaseObjects.Streak;
import main.java.databaseObjects.Submission;
import main.java.databaseObjects.Trainer;
import main.java.databaseObjects.Workout;

import static org.junit.Assert.assertEquals;
import static test.java.databaseObjects.ChallengeTest.getChallengeTestItem;
import static test.java.databaseObjects.ClientTest.getClientTestItem;
import static test.java.databaseObjects.CommentTest.getCommentTestItem;
import static test.java.databaseObjects.EnterpriseTest.getEnterpriseTestItem;
import static test.java.databaseObjects.EventTest.getEventTestItem;
import static test.java.databaseObjects.GroupTest.getGroupTestItem;
import static test.java.databaseObjects.GymTest.getGymTestItem;
import static test.java.databaseObjects.InviteTest.getInviteTestItem;
import static test.java.databaseObjects.MessageTest.getMessageTestItem;
import static test.java.databaseObjects.PostTest.getPostTestItem;
import static test.java.databaseObjects.ReviewTest.getReviewTestItem;
import static test.java.databaseObjects.SponsorTest.getSponsorTestItem;
import static test.java.databaseObjects.StreakTest.getStreakTestItem;
import static test.java.databaseObjects.SubmissionTest.getSubmissionTestItem;
import static test.java.databaseObjects.TrainerTest.getTrainerTestItem;
import static test.java.databaseObjects.WorkoutTest.getWorkoutTestItem;

public class DatabaseItemFactoryTest {
    @Test
    public void testBuildClient() throws Exception {
        Item item = getClientTestItem();
        assertEquals(new Client(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildTrainer() throws Exception {
        Item item = getTrainerTestItem();
        assertEquals(new Trainer(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildGym() throws Exception {
        Item item = getGymTestItem();
        assertEquals(new Gym(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildWorkout() throws Exception {
        Item item = getWorkoutTestItem();
        assertEquals(new Workout(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildReview() throws Exception {
        Item item = getReviewTestItem();
        assertEquals(new Review(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildEvent() throws Exception {
        Item item = getEventTestItem();
        assertEquals(new Event(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildChallenge() throws Exception {
        Item item = getChallengeTestItem();
        assertEquals(new Challenge(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildInvite() throws Exception {
        Item item = getInviteTestItem();
        assertEquals(new Invite(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildPost() throws Exception {
        Item item = getPostTestItem();
        assertEquals(new Post(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildSubmission() throws Exception {
        Item item = getSubmissionTestItem();
        assertEquals(new Submission(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildGroup() throws Exception {
        Item item = getGroupTestItem();
        assertEquals(new Group(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildComment() throws Exception {
        Item item = getCommentTestItem();
        assertEquals(new Comment(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildSponsor() throws Exception {
        Item item = getSponsorTestItem();
        assertEquals(new Sponsor(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildMessage() throws Exception {
        Item item = getMessageTestItem();
        assertEquals(new Message(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildStreak() throws Exception {
        Item item = getStreakTestItem();
        assertEquals(new Streak(item), DatabaseItemFactory.build(item));
    }

    @Test
    public void testBuildEnterprise() throws Exception {
        Item item = getEnterpriseTestItem();
        assertEquals(new Enterprise(item), DatabaseItemFactory.build(item));
    }
}
