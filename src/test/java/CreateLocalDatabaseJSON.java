package test.java;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateChallenge;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateClient;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;
import main.java.logic.Constants;
import main.java.logic.TimeHelper;
import main.java.testing.TestHelper;
import main.java.testing.TestTableHelper;

public class CreateLocalDatabaseJSON {
    @ClassRule
    public static LocalDynamoDBCreationRule rule = new LocalDynamoDBCreationRule();

    @Before
    public void init() throws Exception {
        TestHelper.reinitTablesFromJSON("emptyTable.json", "emptyTable.json");
    }

    /**
     * This method initializes the whole environment from the JSON files indicated above and then
     * after the method finishes will save the resulting database Tables into JSON files
     * "outDB.json" and "outMSG.json" for the database table and message tables respectively.
     *
     * @throws Exception If there was any problems you caused.
     */
    @Test
    public void create() throws Exception {
        String userID = DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest("Leo", null, null,
                        "EMAIL", "LB", null, null, null,
                        null), 0));
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                userID, new CreateChallengeRequest(userID, TimeHelper.isoString(
                        TimeHelper.hoursFromNow(240)), "10", "Test 1 Current N Streak",
                        "To do 1 thing per interval", "streak", null,
                        null, null, null, "private", null,
                        null, null, "daily", "1",
                        "1"), 0));
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                userID, new CreateChallengeRequest(userID, TimeHelper.isoString(
                        TimeHelper.hoursFromNow(240)), "10", "Test 2 Current N Streak",
                        "To do 2 things per interval", "streak", null,
                        null, null, null, "private", null,
                        null, null, "daily", "1",
                        "2"), 0));
    }

    @After
    public void save() throws Exception {
        TestTableHelper.getInstance().saveTableToJSON(DynamoDBHandler.getInstance().client,
                Constants.databaseTableName, "outDB.json");
        TestTableHelper.getInstance().saveTableToJSON(DynamoDBHandler.getInstance().client,
                Constants.messageTableName, "outMSG.json");
    }
}
