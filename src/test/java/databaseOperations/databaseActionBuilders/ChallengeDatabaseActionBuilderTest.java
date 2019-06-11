package test.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ChallengeDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;

import static org.junit.Assert.assertEquals;

public class ChallengeDatabaseActionBuilderTest {
    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    // Create ------------------------------------------------------------------------------------
    @Test
    public void testCreateChallengeAction() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("item_type", new AttributeValue("Challenge"));
        item.put("marker", new AttributeValue().withN("0"));
        item.put("owner", new AttributeValue("Owner"));
        item.put("endTime", new AttributeValue("ENDTIME"));
        item.put("capacity", new AttributeValue("CAPACITY"));
        item.put("title", new AttributeValue("TITLE"));
        item.put("goal", new AttributeValue("GOAL"));
        item.put("ifCompleted", new AttributeValue("false"));
        item.put("challengeType", new AttributeValue("CHALLENGETYPE"));
        item.put("group", new AttributeValue("GROUP"));
        item.put("description", new AttributeValue("DESCRIPTION"));
        item.put("tags", new AttributeValue(Collections.singletonList("TAG")));
        item.put("members", new AttributeValue(Collections.singletonList("MEMBER")));
        item.put("access", new AttributeValue("ACCESS"));
        item.put("restriction", new AttributeValue("RESTRICTION"));
        item.put("difficulty", new AttributeValue("DIFFICULTY"));
        item.put("prize", new AttributeValue("PRIZE"));
        item.put("streakUpdateSpanType", new AttributeValue("STREAKUPDATESPANTYPE"));
        item.put("streakUpdateInterval", new AttributeValue("STREAKUPDATEINTERVAL"));
        item.put("streakN", new AttributeValue("STREAKN"));
        CreateDatabaseAction expectedAction = new CreateDatabaseAction("Challenge", item, new HashMap<>(), null);
        DatabaseAction actualAction = ChallengeDatabaseActionBuilder.create(new CreateChallengeRequest(
                "OWNER", "ENDTIME", "CAPACITY", "TITLE", "GOAL",
                "CHALLENGETYPE", "GROUP", "DESCRIPTION",
                new String[]{"TAG"}, new String[]{"MEMBER"}, "ACCESS", "RESTRICTION",
                "DIFFICULTY", "PRIZE", null, "STREAKUPDATESPANTYPE",
                "STREAKUPDATEINTERVAL", "STREAKN"
        ), new HashMap<>());
        assertEquals(expectedAction, actualAction);
    }

    // Update ------------------------------------------------------------------------------------

    // TODO
    @Test
    public void testUpdateTitle() throws Exception {
        DatabaseAction actualAction = ChallengeDatabaseActionBuilder.updateTitle("CL0001", "TITLE");
    }
//    public static DatabaseAction updateTitle(String id, String title) throws Exception {
//    public static DatabaseAction updateDescription(String id, String description) throws Exception {
//    public static DatabaseAction updateAddress(String id, String address) throws Exception {
//    public static DatabaseAction updateIfCompleted(String id, String ifCompleted) throws Exception {
//    public static DatabaseAction updateGoal(String id, String goal) throws Exception {
//    public static DatabaseAction updateDifficulty(String id, String difficulty) throws Exception {
//    public static DatabaseAction updateEndTime(String id, String endTime) throws Exception {
//    public static DatabaseAction updatePrize(String id, String prize) throws Exception {
//    public static DatabaseAction updateAccess(String id, String access) throws Exception {
//    public static DatabaseAction updateRestriction(String id, String restriction) throws Exception {
//    public static DatabaseAction updateAddMember(String id, String user, boolean ifAccepting) throws Exception {
//    public static DatabaseAction updateRemoveMember(String id, String user) throws Exception {
//    public static DatabaseAction updateAddInvitedMember(String id, String user) throws Exception {
//    public static DatabaseAction updateRemoveInvitedMember(String id, String user) throws Exception {
//    public static DatabaseAction updateAddMemberRequest(String id, String user) throws Exception {
//    public static DatabaseAction updateRemoveMemberRequest(String id, String user) throws Exception {
//    public static DatabaseAction updateAddReceivedInvite(String id, String invite, boolean ifWithCreate) throws
//    public static DatabaseAction updateRemoveReceivedInvite(String id, String invite) throws Exception {
//    public static DatabaseAction updateCapacity(String id, String capacity) throws Exception {
//    public static DatabaseAction updateAddTag(String id, String tag) throws Exception {
//    public static DatabaseAction updateRemoveTag(String id, String tag) throws Exception {
//    public static DatabaseAction updateWinner(String id, String winner) throws Exception {
//    public static DatabaseAction updateAddEvent(String id, String event, boolean ifWithCreate) throws Exception {
//    public static DatabaseAction updateRemoveEvent(String id, String event) throws Exception {
//    public static DatabaseAction updateAddCompletedEvent(String id, String event) throws Exception {
//    public static DatabaseAction updateRemoveCompletedEvent(String id, String event) throws Exception {
//    public static DatabaseAction updateAddSubmission(String id, String submission, boolean ifWithCreate) throws
//    public static DatabaseAction updateRemoveSubmission(String id, String submission) throws Exception {
//    public static DatabaseAction updateGroup(String id, String group) throws Exception {
//    public static DatabaseAction updateAddStreak(String id, String streak, boolean ifWithCreate) throws Exception {
//    public static DatabaseAction updateAddStreak(String aboutIdentifier) throws Exception {
//    public static DatabaseAction updateRemoveStreak(String id, String streak) throws Exception {

    // Delete ------------------------------------------------------------------------------------
    @Test
    public void testDelete() throws Exception {
        PrimaryKey key = new PrimaryKey("item_type", "Challenge", "id", "CH0001");
        DeleteDatabaseAction expectedAction = new DeleteDatabaseAction("CH0001", "Challenge", key);
        assertEquals(expectedAction, ChallengeDatabaseActionBuilder.delete("CH0001"));
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    // Create
}
