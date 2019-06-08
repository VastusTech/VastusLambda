package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Map;

import main.java.databaseObjects.Comment;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CommentTest {
    static Item getCommentTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Comment")
                .withString("id", "CO0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("by", "BY")
                .withString("to", "TO")
                .withString("comment", "COMMENT")
                .withStringSet("likes", Sets.newSet("1", "2", "3"))
                .withStringSet("comments", Sets.newSet("1", "2", "3"));
    }

    private void assertEmptySet(Object actual) {
        assertEquals(Sets.newSet(), actual);
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testGetEmptyItem() {
        Map<String, AttributeValue> emptyItem = Comment.getEmptyItem();
        assertEquals("Comment", emptyItem.get("item_type").getS());
    }

    @Test
    public void testComment() throws Exception {
        Comment comment = new Comment(getCommentTestItem());
        assertEquals("Comment", comment.itemType);
        assertEquals("CO0001", comment.id);
        assertEquals(1, comment.marker);
        assertNotNull(comment.timeCreated);
        assertEquals("BY", comment.by);
        assertEquals("TO", comment.to);
        assertEquals("COMMENT", comment.comment);
        assertEquals(Sets.newSet("1", "2", "3"), comment.likes);
        assertEquals(Sets.newSet("1", "2", "3"), comment.comments);
    }

    @Test
    public void testCommentNullBy() throws Exception {
        assertNull((new Comment(getCommentTestItem().with("by", null))).by);
    }

    @Test
    public void testCommentNullTo() throws Exception {
        assertNull((new Comment(getCommentTestItem().with("to", null))).to);
    }

    @Test
    public void testCommentNullComment() throws Exception {
        assertNull((new Comment(getCommentTestItem().with("comment", null))).comment);
    }

    @Test
    public void testCommentNullLikes() throws Exception {
        assertEmptySet((new Comment(getCommentTestItem().with("likes", null))).likes);
    }

    @Test
    public void testCommentNullComments() throws Exception {
        assertEmptySet((new Comment(getCommentTestItem().with("comments", null))).comments);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failCommentWrongItemTypeAndID() throws Exception {
        new Comment(getCommentTestItem().withString("item_type", "Trainer")
                .withString("id", "TR0001"));
    }
}
