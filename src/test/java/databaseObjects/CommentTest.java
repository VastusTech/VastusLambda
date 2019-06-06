package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import main.java.databaseObjects.Comment;
import main.java.logic.TimeHelper;

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

    @Test
    public void test() {
        Comment comment;
    }
}
