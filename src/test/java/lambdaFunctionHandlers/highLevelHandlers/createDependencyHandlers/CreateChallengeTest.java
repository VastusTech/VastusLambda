package test.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.Client;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.Post;
import main.java.databaseObjects.Trainer;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateChallenge;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class CreateChallengeTest {
    @ClassRule
    public static LocalDynamoDBCreationRule rule = new LocalDynamoDBCreationRule();

    @Before
    public void init() throws Exception {
        TestHelper.reinitTablesFromJSON("table1.json", "table1.json");;
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void createBareChallenge() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                Constants.adminKey, new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertNull(challenge.group);
        assertNull(challenge.description);
        assertNull(challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertNull(challenge.prize);
        assertNull(challenge.streakN);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakUpdateSpanType);
        assertTrue(challenge.tags.isEmpty());
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
    }

    @Test
    public void createChallengeWithExtraInfo() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                Constants.adminKey, new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, "DESCRIPTION",
                        new String[]{"Endurance", "HIIT"}, new String[]{"CL0001"}, "private",
                        null, "1", "PRIZE",
                        null, null, null
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertNull(challenge.group);
        assertEquals("DESCRIPTION", challenge.description);
        assertNull(challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertEquals("PRIZE", challenge.prize);
        assertNull(challenge.streakN);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakUpdateSpanType);
        assertEquals(2, challenge.tags.size());
        assertTrue(challenge.tags.contains("Endurance"));
        assertTrue(challenge.tags.contains("HIIT"));
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
    }

    // Group Challenge
    @Test
    public void createGroupChallenge() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                Constants.adminKey, new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, "GR0001", null,
                        null, null, null, null, null,
                        null, null, null, null
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertEquals("GR0001", challenge.group);
        assertNull(challenge.description);
        assertNull(challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertNull(challenge.prize);
        assertNull(challenge.streakN);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakUpdateSpanType);
        assertTrue(challenge.tags.isEmpty());
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
        Group group = Group.readGroup("GR0001");
        assertTrue(group.challenges.contains(id));
        assertEquals(challenge.access, group.access);
    }

    @Test
    public void createGroupChallengeWithExtraInfo() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                Constants.adminKey, new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, "GR0001", "DESCRIPTION",
                        new String[]{"Endurance", "HIIT"}, new String[]{"CL0001"}, null,
                        null, "1", "PRIZE",
                        null, null, null
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertEquals("GR0001", challenge.group);
        assertEquals("DESCRIPTION", challenge.description);
        assertNull(challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertEquals("PRIZE", challenge.prize);
        assertNull(challenge.streakN);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakUpdateSpanType);
        assertEquals(2, challenge.tags.size());
        assertTrue(challenge.tags.contains("Endurance"));
        assertTrue(challenge.tags.contains("HIIT"));
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
        Group group = Group.readGroup("GR0001");
        assertTrue(group.challenges.contains(id));
        assertEquals(challenge.access, group.access);
    }

    // Streak Challenge
    @Test
    public void createStreakChallenge() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                Constants.adminKey, new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", "streak", null, null,
                        null, null, "private", null, null,
                        null, "daily", "1", "1"
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertEquals("streak", challenge.challengeType);
        assertNull(challenge.group);
        assertNull(challenge.description);
        assertNull(challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertNull(challenge.prize);
        assertEquals("1", challenge.streakN);
        assertEquals("1", challenge.streakUpdateInterval);
        assertEquals("daily", challenge.streakUpdateSpanType);
        assertTrue(challenge.tags.isEmpty());
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
    }
    @Test
    public void createStreakChallengeWithExtraInfo() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                Constants.adminKey, new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", "streak", null, "DESCRIPTION",
                        new String[]{"Endurance", "HIIT"}, new String[]{"CL0001"}, "private",
                        null, "1", "PRIZE",
                        "daily", "1", "1"
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertEquals("streak", challenge.challengeType);
        assertNull(challenge.group);
        assertEquals("DESCRIPTION", challenge.description);
        assertNull(challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertEquals("PRIZE", challenge.prize);
        assertEquals("1", challenge.streakN);
        assertEquals("1", challenge.streakUpdateInterval);
        assertEquals("daily", challenge.streakUpdateSpanType);
        assertEquals(2, challenge.tags.size());
        assertTrue(challenge.tags.contains("Endurance"));
        assertTrue(challenge.tags.contains("HIIT"));
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
    }

    // Restricted Challenge
    @Test
    public void createRestrictedChallenge() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                Constants.adminKey, new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", "invite", null,
                        null, null, null, null
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertNull(challenge.group);
        assertNull(challenge.description);
        assertEquals("invite", challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertNull(challenge.prize);
        assertNull(challenge.streakN);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakUpdateSpanType);
        assertTrue(challenge.tags.isEmpty());
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
    }

    @Test
    public void createRestrictedChallengeWithExtraInfo() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                Constants.adminKey, new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, "DESCRIPTION",
                        new String[]{"Endurance", "HIIT"}, new String[]{"CL0001"}, "private",
                        "invite", "1", "PRIZE",
                        null, null, null
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertNull(challenge.group);
        assertEquals("DESCRIPTION", challenge.description);
        assertEquals("invite", challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertEquals("PRIZE", challenge.prize);
        assertNull(challenge.streakN);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakUpdateSpanType);
        assertEquals(2, challenge.tags.size());
        assertTrue(challenge.tags.contains("Endurance"));
        assertTrue(challenge.tags.contains("HIIT"));
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
    }

    // Challenge by Client
    @Test
    public void createBareChallengeByClient() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertNull(challenge.group);
        assertNull(challenge.description);
        assertNull(challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertNull(challenge.prize);
        assertNull(challenge.streakN);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakUpdateSpanType);
        assertTrue(challenge.tags.isEmpty());
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
    }

    // Challenge with existing members
    @Test
    public void createChallengeWithExistingMembers() throws Exception {
        Set<String> oldClientPosts = Client.readClient("CL0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, new String[]{"CL0001", "CL0002"}, "private",
                        null, null, null, null,
                        null, null
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("CL0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertNull(challenge.group);
        assertNull(challenge.description);
        assertNull(challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertNull(challenge.prize);
        assertNull(challenge.streakN);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakUpdateSpanType);
        assertTrue(challenge.tags.isEmpty());
        assertEquals(2, challenge.members.size());
        assertTrue(challenge.members.contains("CL0001"));
        Client client = Client.readClient("CL0001");
        assertTrue(client.challenges.contains(id));
        assertTrue(client.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(client.posts);
        newPosts.removeAll(oldClientPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("CL0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
    }
    // Trainer public Challenge
    @Test
    public void createTrainerPublicChallenge() throws Exception {
        Set<String> oldTrainerPosts = Trainer.readTrainer("TR0001").posts;
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "TR0001", new CreateChallengeRequest(
                        "TR0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "public", null, null,
                        null, null, null, null
                ), 0
        ));
        Challenge challenge = Challenge.readChallenge(id);
        assertEquals("TR0001", challenge.owner);
        assertEquals(new DateTime(1998, 9, 5,
                0, 0), challenge.endTime);
        assertEquals(100, challenge.capacity);
        assertEquals("TITLE", challenge.title);
        assertEquals("GOAL", challenge.goal);
        assertNull(challenge.challengeType);
        assertNull(challenge.group);
        assertNull(challenge.description);
        assertNull(challenge.restriction);
        assertEquals(1, challenge.difficulty);
        assertNull(challenge.prize);
        assertNull(challenge.streakN);
        assertNull(challenge.streakUpdateInterval);
        assertNull(challenge.streakUpdateSpanType);
        assertTrue(challenge.tags.isEmpty());
        assertEquals(1, challenge.members.size());
        assertTrue(challenge.members.contains("TR0001"));
        Trainer trainer = Trainer.readTrainer("TR0001");
        assertTrue(trainer.challenges.contains(id));
        assertTrue(trainer.ownedChallenges.contains(id));
        Set<String> newPosts = new HashSet<>(trainer.posts);
        newPosts.removeAll(oldTrainerPosts);
        assertEquals(1, newPosts.size());
        Post post = Post.readPost(newPosts.iterator().next());
        assertEquals("TR0001", post.by);
        assertEquals(challenge.access, post.access);
        assertEquals(challenge.group, post.group);
        assertEquals("newChallenge", post.postType);
        assertEquals(id, post.about);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertTrue(post.likes.isEmpty());
        assertTrue(post.comments.isEmpty());
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    // TODO

    // Client create public challenge
    @Test(expected = Exception.class)
    public void failClientPublicChallenge() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "public", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    // From ID not owner
    @Test(expected = Exception.class)
    public void failNotSameFromID() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0002", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoFromID() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                null, new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoOwner() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        null, "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoEndtime() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", null, "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoCapacity() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", null, "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoTitle() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", null,
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoGoal() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        null, null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failOwnerInvalid() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "OWNER", new CreateChallengeRequest(
                        "OWNER", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failExistingMembersNotFriends() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, new String[]{"CL0003"}, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failBadEndtime() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "NOT-A-GOOD-ENDTIME", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failBadCapacity() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "CAPACITY", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failBadGroup() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, "BAD-GROUP", null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failOwnerNotAOwnerOfGroup() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0002", new CreateChallengeRequest(
                        "CL0002", "1998-09-05", "100", "TITLE",
                        "GOAL", null, "GR0001", null,
                        null, null, "private", null, null,
                        null, null, null, null
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failBadDifficulty1() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null,
                        "DIFFICULTY", null, null,
                        null, null
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failBadDifficulty2() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null,
                        "5", null, null,
                        null, null
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failStreakInfoWithoutStreakType() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", null, null, null,
                        null, null, "private", null,
                        "5", null, "daily",
                        "1", "1"
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failBadStreakN() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", "streak", null, null,
                        null, null, "private", null,
                        "5", null, "daily",
                        "1", "STREAKN"
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failBadStreakUpdateInterval() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", "streak", null, null,
                        null, null, "private", null,
                        "5", null, "daily",
                        "INTERVAL", "1"
                ), 0
        ));
    }
    @Test(expected = Exception.class)
    public void failBadStreakUpdateSpanType() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                "CL0001", new CreateChallengeRequest(
                        "CL0001", "1998-09-05", "100", "TITLE",
                        "GOAL", "streak", null, null,
                        null, null, "private", null,
                        "5", null, "TYPE",
                        "1", "1"
                ), 0
        ));
    }
}
