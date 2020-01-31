package test.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Set;

import main.java.databaseObjects.Client;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.Post;
import main.java.databaseObjects.Trainer;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreatePost;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class CreatePostTest {
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
    public void createBarePost() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", null, null, null, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertNull(post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
    }

    @Test
    public void createPostWithPictures() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", null, new String[]{"PICTUREPATH1",
                        "PICTUREPATH2"}, null, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertEquals(2, post.picturePaths.size());
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH1"));
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH2"));
        assertTrue(post.videoPaths.isEmpty());
        assertNull(post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
    }

    @Test
    public void createPostWithVideos() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", null, null,
                        new String[]{"VIDEOPATH1", "VIDEOPATH2"}, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertEquals(2, post.videoPaths.size());
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH1"));
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH2"));
        assertNull(post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
    }

    @Test
    public void createPostWithPicturesAndVideos() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", null, new String[]{"PICTUREPATH1",
                        "PICTUREPATH2"}, new String[]{"VIDEOPATH1", "VIDEOPATH2"}, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertEquals(2, post.picturePaths.size());
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH1"));
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH2"));
        assertEquals(2, post.videoPaths.size());
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH1"));
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH2"));
        assertNull(post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
    }

    @Test
    public void createSharePost() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", "CH0001",
                        "private", "Challenge", null, null, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertEquals("CH0001", post.about);
        assertEquals("Challenge", post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertNull(post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
    }

    @Test
    public void createSharePostWithPictures() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", "CH0001",
                        "private", "Challenge", new String[]{"PICTUREPATH1",
                        "PICTUREPATH2"}, null, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertEquals("CH0001", post.about);
        assertEquals("Challenge", post.postType);
        assertEquals(2, post.picturePaths.size());
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH1"));
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH2"));
        assertTrue(post.videoPaths.isEmpty());
        assertNull(post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
    }

    @Test
    public void createSharePostWithVideos() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", "CH0001",
                        "private", "Challenge", null,
                        new String[]{"VIDEOPATH1", "VIDEOPATH2"}, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertEquals("CH0001", post.about);
        assertEquals("Challenge", post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertEquals(2, post.videoPaths.size());
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH1"));
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH2"));
        assertNull(post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
    }

    @Test
    public void createSharePostWithPicturesAndVideos() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", "CH0001",
                        "private", "Challenge", new String[]{"PICTUREPATH1",
                        "PICTUREPATH2"}, new String[]{"VIDEOPATH1", "VIDEOPATH2"}, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertEquals("CH0001", post.about);
        assertEquals("Challenge", post.postType);
        assertEquals(2, post.picturePaths.size());
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH1"));
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH2"));
        assertEquals(2, post.videoPaths.size());
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH1"));
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH2"));
        assertNull(post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
    }

    @Test
    public void createGroupPost() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        null, null, null,
                        null, "GR0001"
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertEquals("GR0001", post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
        assertTrue(Group.readGroup("GR0001").posts.contains(id));
    }

    @Test
    public void createGroupPostWithPictures() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        null, null, new String[]{"PICTUREPATH1",
                        "PICTUREPATH2"}, null, "GR0001"
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertEquals(2, post.picturePaths.size());
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH1"));
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH2"));
        assertTrue(post.videoPaths.isEmpty());
        assertEquals("GR0001", post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
        assertTrue(Group.readGroup("GR0001").posts.contains(id));
    }

    @Test
    public void createGroupPostWithVideos() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", null, null,
                        new String[]{"VIDEOPATH1", "VIDEOPATH2"}, "GR0001"
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertEquals(2, post.videoPaths.size());
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH1"));
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH2"));
        assertEquals("GR0001", post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
        assertTrue(Group.readGroup("GR0001").posts.contains(id));
    }

    @Test
    public void createGroupPostWithPicturesAndVideos() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        null, null, new String[]{"PICTUREPATH1",
                        "PICTUREPATH2"}, new String[]{"VIDEOPATH1", "VIDEOPATH2"}, "GR0001"
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertEquals(2, post.picturePaths.size());
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH1"));
        assertTrue(post.picturePaths.contains(id + "/PICTUREPATH2"));
        assertEquals(2, post.videoPaths.size());
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH1"));
        assertTrue(post.videoPaths.contains(id + "/VIDEOPATH2"));
        assertEquals("GR0001", post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
        assertTrue(Group.readGroup("GR0001").posts.contains(id));
    }

    @Test
    public void createGroupPostNotOwner() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0002", "DESCRIPTION", null,
                        null, null, null,
                        null, "GR0001"
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0002", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertEquals("GR0001", post.group);
        assertTrue(Client.readClient("CL0002").posts.contains(id));
        assertTrue(Group.readGroup("GR0001").posts.contains(id));
    }

    @Test
    public void createBarePostAsClient() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "CL0001", new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", null, null, null, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("CL0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertNull(post.group);
        assertTrue(Client.readClient("CL0001").posts.contains(id));
    }

    @Test
    public void createBarePostAsTrainer() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "TR0001", new CreatePostRequest(
                        "TR0001", "DESCRIPTION", null,
                        "private", null, null, null, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("TR0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("private", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertNull(post.group);
        assertTrue(Trainer.readTrainer("TR0001").posts.contains(id));
    }

    @Test
    public void createPublicPostAsTrainer() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "TR0001", new CreatePostRequest(
                        "TR0001", "DESCRIPTION", null,
                        "public", null, null, null, null
                ), 0, null
        ));
        Post post = Post.readPost(id);
        assertEquals("TR0001", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("public", post.access);
        assertNull(post.about);
        assertNull(post.postType);
        assertTrue(post.picturePaths.isEmpty());
        assertTrue(post.videoPaths.isEmpty());
        assertNull(post.group);
        assertTrue(Trainer.readTrainer("TR0001").posts.contains(id));
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = Exception.class)
    public void failFromIDWrongClient() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "CL0002", new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", null, null, null, null
                ), 0, null
        ));
    }

    @Test(expected = Exception.class)
    public void failFromIDWrongTrainer() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "TR0002", new CreatePostRequest(
                        "TR0001", "DESCRIPTION", null,
                        "private", null, null, null, null
                ), 0, null
        ));
    }

    @Test(expected = Exception.class)
    public void failByNotUser() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "CH0001", new CreatePostRequest(
                        "CH0001", "DESCRIPTION", null,
                        "private", null, null, null, null
                ), 0, null
        ));
    }

    @Test(expected = Exception.class)
    public void failFromIDClientNotInGroup() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "CL0003", new CreatePostRequest(
                        "CL0003", "DESCRIPTION", null,
                        "private", null, null,
                        null, "GR0001"
                ), 0, null
        ));
    }

    @Test(expected = Exception.class)
    public void failFromIDTrainerNotInGroup() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "TR0001", new CreatePostRequest(
                        "TR0001", "DESCRIPTION", null,
                        "private", null, null,
                        null, "GR0001"
                ), 0, null
        ));
    }

    @Test(expected = Exception.class)
    public void failPublicClientPost() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "public", null, null, null, null
                ), 0, null
        ));
    }
    @Test(expected = Exception.class)
    public void failShareNullItemPost() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", "Challenge", null, null, null
                ), 0, null
        ));
    }
    @Test(expected = Exception.class)
    public void failShareBadItemPost() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", "ABOUT",
                        "private", "Challenge", null, null, null
                ), 0, null
        ));
    }
    @Test(expected = Exception.class)
    public void failShareNullPostType() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", "CH0001",
                        "private", null, null, null,
                        null
                ), 0, null
        ));
    }
    @Test(expected = Exception.class)
    public void failShareBadPostType() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", "CH0001",
                        "private", "Not-a-Challenge", null, null,
                        null
                ), 0, null
        ));
    }
    @Test(expected = Exception.class)
    public void failShareNotMatchingPostType() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                Constants.adminKey, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", "CH0001",
                        "private", "Not-a-Challenge", null, null,
                        null
                ), 0, null
        ));
    }
    @Test(expected = Exception.class)
    public void failPostBadGroup() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "CL0001", new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", null, null,
                        null, "GR0003"
                ), 0, null
        ));
    }

    @Test(expected = Exception.class)
    public void failNoAccess() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "CL0001", new CreatePostRequest(
                        "CL0001", null, null,
                        null, null, null,
                        null, null
                ), 0, null
        ));
    }

    @Test(expected = Exception.class)
    public void failInvalidAccess() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "CL0001", new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "ACCESS", null, null,
                        null, null
                ), 0, null
        ));
    }

    @Test(expected = Exception.class)
    public void failNoFromID() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                null, new CreatePostRequest(
                        "CL0001", "DESCRIPTION", null,
                        "private", null, null,
                        null, null
                ), 0, null
        ));
    }

    @Test(expected = Exception.class)
    public void failNoBy() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
                "CL0001", new CreatePostRequest(
                        null, "DESCRIPTION", null,
                        "private", null, null,
                        null, null
                ), 0, null
        ));
    }
}
