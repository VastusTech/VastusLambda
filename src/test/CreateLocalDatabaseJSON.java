package test;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateChallenge;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateClient;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateComment;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateEvent;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateGroup;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateGym;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateInvite;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateMessage;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreatePost;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateReview;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateSponsor;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateStreak;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateSubmission;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateTrainer;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateWorkout;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreateEventRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGymRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreateInviteRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreateTrainerRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreateWorkoutRequest;
import main.java.logic.Constants;
import main.java.logic.TimeHelper;
import main.java.testing.TestHelper;
import main.java.testing.TestTableHelper;
import test.java.LocalDynamoDBCreationRule;

/**
 * The JUnit Test Class for creating a local database JSON isolated from the rest of the testing
 * suite.
 */
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
                        null, null, null, "daily", "1",
                        "1"), 0));
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                userID, new CreateChallengeRequest(userID, TimeHelper.isoString(
                        TimeHelper.hoursFromNow(240)), "10", "Test 2 Current N Streak",
                        "To do 2 things per interval", "streak", null,
                        null, null, null, "private", null,
                        null, null, null, "daily", "1",
                        "2"), 0));
        DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
                userID, new CreateChallengeRequest(userID, TimeHelper.isoString(
                        TimeHelper.hoursFromNow(240)), "10", "Test 2 Current N 2 " +
                        "update interval Streak", "To do 2 things per 2 intervals",
                        "streak", null, null, null, null,
                        "private", null, null, null, null,
                        "daily", "2", "2"), 0));
    }

    @Test
    public void createGrabBagTable() throws Exception {
        String clientID = DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest("Leo", "GENDER",
                        "1998-10-05", "EMAIL", "LB", "BIO", null,
                        null, null), 0
        ));
        String trainerID = DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest("Leo", "GENDER",
                        "1998-10-05", "EMAIL", "LB", null,
                        null, null, null, null,
                        "BIO", null, null ), 0
        ));
//        String gymID = DynamoDBHandler.getInstance().attemptTransaction(CreateGym.getCompilers(
//                Constants.adminKey, new CreateGymRequest("Leo", "EMAIL",
//                        "LB", "1998-10-05", null, null,
//                        "BIO", "ADDRESS", null, "10",
//                        "TYPE", "10"), 0
//        ));
//        String workoutID = DynamoDBHandler.getInstance().attemptTransaction(CreateWorkout.getCompilers(
//                Constants.adminKey, new CreateWorkoutRequest(), 0
//        ));
//        String reviewID = DynamoDBHandler.getInstance().attemptTransaction(CreateReview.getCompilers(
//                Constants.adminKey, new CreateClientRequest("Leo", "GENDER",
//                        "1998-10-05", "EMAIL", "LB", "BIO", null,
//                        null, null), 0
//        ));
//        String eventID = DynamoDBHandler.getInstance().attemptTransaction(CreateEvent.getCompilers(
//                Constants.adminKey, new CreateEventRequest(trainerID, TimeITimeHelper.isoString(
//                        TimeHelper.hoursFromNow(12)), "10", "ADDRESS",
//                        "TITLE", null, "DESCRIPTION", null,
//                        null, "public", null, null), 0
//        ));
//        String challengeID = DynamoDBHandler.getInstance().attemptTransaction(CreateChallenge.getCompilers(
//                Constants.adminKey, new CreateChallengeRequest(trainerID, ), 0
//        ));
//        String inviteID = DynamoDBHandler.getInstance().attemptTransaction(CreateInvite.getCompilers(
//                Constants.adminKey, new CreateInviteRequest(clientID, trainerID, "friendRequest",
//                        clientID, "DESCRIPTION"), 0
//        ));
//        String postID = DynamoDBHandler.getInstance().attemptTransaction(CreatePost.getCompilers(
//                Constants.adminKey, new CreatePostRequest(clientID, "DESCRIPTION"), 0
//        ));
//        String submissionID = DynamoDBHandler.getInstance().attemptTransaction(CreateSubmission.getCompilers(
//                Constants.adminKey, new CreateClientRequest("Leo", "GENDER",
//                        "1998-10-05", "EMAIL", "LB", "BIO", null,
//                        null, null), 0
//        ));
//        String groupID = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
//                Constants.adminKey, new CreateClientRequest("Leo", "GENDER",
//                        "1998-10-05", "EMAIL", "LB", "BIO", null,
//                        null, null), 0
//        ));
//        String commentID = DynamoDBHandler.getInstance().attemptTransaction(CreateComment.getCompilers(
//                Constants.adminKey, new CreateClientRequest("Leo", "GENDER",
//                        "1998-10-05", "EMAIL", "LB", "BIO", null,
//                        null, null), 0
//        ));
//        String sponsorID = DynamoDBHandler.getInstance().attemptTransaction(CreateSponsor.getCompilers(
//                Constants.adminKey, new CreateClientRequest("Leo", "GENDER",
//                        "1998-10-05", "EMAIL", "LB", "BIO", null,
//                        null, null), 0
//        ));
//        String streakID = DynamoDBHandler.getInstance().attemptTransaction(CreateStreak.getCompilers(
//                Constants.adminKey, new CreateClientRequest("Leo", "GENDER",
//                        "1998-10-05", "EMAIL", "LB", "BIO", null,
//                        null, null), 0
//        ));
//        String messageID = DynamoDBHandler.getInstance().attemptTransaction(CreateMessage.getCompilers(
//                Constants.adminKey, new CreateClientRequest("Leo", "GENDER",
//                        "1998-10-05", "EMAIL", "LB", "BIO", null,
//                        null, null), 0
//        ));
    }

    @After
    public void save() throws Exception {
        TestTableHelper.getInstance().saveTableToJSON(DynamoDBHandler.getInstance().client,
                Constants.databaseTableName, "outDB.json");
        TestTableHelper.getInstance().saveTableToJSON(DynamoDBHandler.getInstance().client,
                Constants.messageTableName, "outMSG.json");
    }
}
