package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Map;

import main.java.databaseObjects.Post;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PostTest {
    static Item getPostTestItem() {
        return new Item()
                .withString("item_type", "Post")
                .withString("id", "PO0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("by", "BY")
                .withString("description", "DESCRIPTION")
                .withString("about", "ABOUT")
                .withString("access", "ACCESS")
                .withString("postType", "POSTTYPE")
                .withStringSet("picturePaths", Sets.newSet("1", "2", "3"))
                .withStringSet("videoPaths", Sets.newSet("1", "2", "3"))
                .withStringSet("likes", Sets.newSet("1", "2", "3"))
                .withStringSet("comments", Sets.newSet("1", "2", "3"))
                .withString("group", "GROUP") ;
    }

    private void assertEmptySet(Object actual) {
        assertEquals(Sets.newSet(), actual);
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testGetEmptyItem() {
        Map<String, AttributeValue> emptyItem = Post.getEmptyItem();
        assertEquals("Post", emptyItem.get("item_type").getS());
    }

    @Test
    public void testPost() throws Exception {
        Post post = new Post(getPostTestItem());
        assertEquals("Post", post.itemType);
        assertEquals("PO0001", post.id);
        assertEquals(1, post.marker);
        assertNotNull(post.timeCreated);
        assertEquals("BY", post.by);
        assertEquals("DESCRIPTION", post.description);
        assertEquals("ABOUT", post.about);
        assertEquals("ACCESS", post.access);
        assertEquals("POSTTYPE", post.postType);
        assertEquals(Sets.newSet("1", "2", "3"), post.picturePaths);
        assertEquals(Sets.newSet("1", "2", "3"), post.videoPaths);
        assertEquals(Sets.newSet("1", "2", "3"), post.likes);
        assertEquals(Sets.newSet("1", "2", "3"), post.comments);
        assertEquals("GROUP", post.group);
    }

    @Test
    public void testPostWithNullDescription() throws Exception {
        assertNull((new Post(getPostTestItem().with("description", null))).description);
    }

    @Test
    public void testPostWithNullAccess() throws Exception {
        assertNull((new Post(getPostTestItem().with("access", null))).access);
    }

    @Test
    public void testPostWithNullAbout() throws Exception {
        assertNull((new Post(getPostTestItem().with("about", null))).about);
    }

    @Test
    public void testPostWithNullPostType() throws Exception {
        assertNull((new Post(getPostTestItem().with("postType", null))).postType);
    }

    @Test
    public void testPostWithNullPicturePaths() throws Exception {
        assertEmptySet((new Post(getPostTestItem().with("picturePaths", null))).picturePaths);
    }

    @Test
    public void testPostWithNullVideoPaths() throws Exception {
        assertEmptySet((new Post(getPostTestItem().with("videoPaths", null))).videoPaths);
    }

    @Test
    public void testPostWithNullLikes() throws Exception {
        assertEmptySet((new Post(getPostTestItem().with("likes", null))).likes);
    }

    @Test
    public void testPostWithNullComments() throws Exception {
        assertEmptySet((new Post(getPostTestItem().with("comments", null))).comments);
    }

    @Test
    public void testPostWithNullGroup() throws Exception {
        assertNull((new Post(getPostTestItem().with("group", null))).group);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failPostWrongItemTypeAndID() throws Exception {
        new Post(getPostTestItem().withString("item_type", "Client")
                .withString("id", "CL0001"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failPostNullBy() throws Exception {
        new Post(getPostTestItem().with("by", null));
    }
}
