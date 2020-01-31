package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Map;

import main.java.databaseObjects.Submission;
import main.java.databaseOperations.exceptions.CorruptedItemException;
import main.java.logic.TimeHelper;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class SubmissionTest {
    static Item getSubmissionTestItem() {
        return new Item()
                .withString("item_type", "Submission")
                .withString("id", "SU0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("by", "BY")
                .withString("description", "DESCRIPTION")
                .withString("about", "ABOUT")
                .withStringSet("picturePaths", Sets.newSet("1", "2", "3"))
                .withStringSet("videoPaths", Sets.newSet("1", "2", "3"))
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
        Map<String, AttributeValue> emptyItem = Submission.getEmptyItem();
        assertEquals("Submission", emptyItem.get("item_type").getS());
    }

    @Test
    public void testSubmission() throws Exception {
        Submission submission = new Submission(getSubmissionTestItem());
        assertEquals("Submission", submission.itemType);
        assertEquals("SU0001", submission.id);
        assertEquals(1, submission.marker);
        assertNotNull(submission.timeCreated);
        assertEquals("BY", submission.by);
        assertEquals("DESCRIPTION", submission.description);
        assertEquals("ABOUT", submission.about);
        assertEquals(Sets.newSet("1", "2", "3"), submission.picturePaths);
        assertEquals(Sets.newSet("1", "2", "3"), submission.videoPaths);
        assertEquals(Sets.newSet("1", "2", "3"), submission.likes);
        assertEquals(Sets.newSet("1", "2", "3"), submission.comments);
    }

    @Test
    public void testSubmissionWithNullDescription() throws Exception {
        assertNull((new Submission(getSubmissionTestItem().with("description", null))).description);
    }

    @Test
    public void testPostWithNullPicturePaths() throws Exception {
        assertEmptySet((new Submission(getSubmissionTestItem().with("picturePaths", null))).picturePaths);
    }

    @Test
    public void testPostWithNullVideoPaths() throws Exception {
        assertEmptySet((new Submission(getSubmissionTestItem().with("videoPaths", null))).videoPaths);
    }

    @Test
    public void testPostWithNullLikes() throws Exception {
        assertEmptySet((new Submission(getSubmissionTestItem().with("likes", null))).likes);
    }

    @Test
    public void testPostWithNullComments() throws Exception {
        assertEmptySet((new Submission(getSubmissionTestItem().with("comments", null))).comments);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = CorruptedItemException.class)
    public void failSubmissionWrongItemTypeAndID() throws Exception {
        new Submission(getSubmissionTestItem().withString("item_type", "Client")
                .withString("id", "CL0001"));
    }

    @Test(expected = CorruptedItemException.class)
    public void failSubmissionNullBy() throws Exception {
        new Submission(getSubmissionTestItem().with("by", null));
    }

    @Test(expected = CorruptedItemException.class)
    public void failSubmissionWithNullAbout() throws Exception {
        new Submission(getSubmissionTestItem().with("about", null));
    }

}
