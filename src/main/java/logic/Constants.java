package main.java.logic;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.Optional;

/**
 * This handles all of the Constants that are retrieved from the Lambda invocation environment
 * variables. Also stores the logger in order to do debugging statements.
 */
public class Constants {
    // THIS GETS IT FROM THE ENVIRONMENTAL VARIABLES FROM AWS LAMBDA (with encryption ;))
    public static boolean ifTesting = false;

    public static int tokenCreditValue = Integer.parseInt(Optional.ofNullable(System.getenv(
            "tokenCreditValue")).orElse("1000"));

    private static LambdaLogger logger = null;
    public static boolean ifDebug = Boolean.parseBoolean(Optional.ofNullable(
            System.getenv("ifDebug")).orElse("false"));

    public static int workoutShortestTimeSectionInterval = Integer.parseInt(Optional.ofNullable(
            System.getenv("workoutShortestTimeSectionInterval")).orElse("15"));

    // ID Creation stuff
    public static int idLength = Integer.parseInt(Optional.ofNullable(
            System.getenv("idLength")).orElse("14"));
    public static int numPrefix = Integer.parseInt(Optional.ofNullable(
            System.getenv("numPrefix")).orElse("2"));

    // Access Materials
    public static boolean ifDevelopment = false;

    // Database Tables
    public static String databaseTableName = Optional.ofNullable(System.getenv(
            "databaseTableName")).orElse("Classics");
//    public static String firebaseTokenTableName = Optional.ofNullable(System.getenv(
//            "firebaseTokenTableName")).orElse("FirebaseTokens");
    public static String messageTableName = Optional.ofNullable(System.getenv(
            "messageTableName")).orElse("Messages");

    // Development Database Tables
    public static String developmentDatabaseTableName = Optional.ofNullable(System.getenv(
            "developmentDatabaseTableName")).orElse("ClassicsTest");
    public static String developmentMessageTableName = Optional.ofNullable(System.getenv(
            "developmentMessageTableName")).orElse("MessagesTest");


//    public static String firebaseFunctionName = Optional.ofNullable(System.getenv(
//            "firebaseFunctionName")).orElse("VastusFirebaseLambdaFunction");
    public static String ablyFunctionName = Optional.ofNullable(System.getenv(
            "ablyFunctionName")).orElse("VastusAblyLambdaFunction");
//    public static String storageBucketName = Optional.ofNullable(System.getenv(
//            "storageBucketName")).orElse("vastusofficial");

    // Potential usage????
//    public static int timeoutSeconds = Integer.parseInt(Optional.ofNullable(System.getenv("timeoutSeconds")).orElse("10"));

    // Secret Information????
    // TODO If we ever want to handle Cognito stuff here?
//    public static String userPoolName = System.getenv("userPoolName");
//    public static String userPoolID = System.getenv("userPoolID");
//    public static String userPoolClientID = System.getenv("userPoolClientID");
//    public static String userPoolSecretKey = System.getenv("userPoolSecretKey");
    public static String adminKey = Optional.ofNullable(System.getenv("adminKey")).
            orElse("admin");

    // Database Limits TODO ENFORCE
    public static int hardStringLengthLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("hardStringLengthLimit")).orElse("100000"));
    public static int userProfileImagePathsLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("userProfileImagePathsLimit")).orElse("10"));
    public static int userOwnedEventsLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("userOwnedEventsLimit")).orElse("100"));
    public static int userOwnedChallengesLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("userOwnedChallengesLimit")).orElse("100"));
    public static int userOwnedGroupsLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("userOwnedGroupsLimit")).orElse("100"));
    public static int userSentInvitesLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("userSentInvitesLimit")).orElse("1000"));
    public static int postPicturePathsLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("postPicturePathsLimit")).orElse("100"));
    public static int postVideoPathsLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("postVideoPathsLimit")).orElse("50"));

    public static void setIfDevelopment(boolean ifDevelopment) {
        Constants.ifDevelopment = ifDevelopment;
    }

    /**
     * Logs to the AWS Lambda logger, using the logger obtained from the context of the call.
     *
     * @param message The message to log.
     */
    public static void debugLog(String message) {
        if (ifDebug) {
            if (Constants.logger == null) {
                System.out.println(message);
            }
            else {
                if (message != null) {
                    Constants.logger.log(message + '\n');
                }
                else {
                    Constants.logger.log("Tried to log a null message!\n");
                }
            }
        }
    }

    /**
     * Sets the AWS Lambda logger from the Context of the Lambda execution.
     *
     * @param logger The logger from the context to initialize the local logger with.
     */
    public static void setLogger(LambdaLogger logger) {
        Constants.logger = logger;
    }
}
