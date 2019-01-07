package main.java.Logic;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.Optional;

public class Constants {
    // TODO THIS GETS IT FROM THE ENVIRONMENTAL VARIABLES FROM AWS LAMBDA (with encryption ;))
    // TODO PUT THESE: workoutShortestTimeSectionInterval, idLength, numPrefix, databaseTableName, storageBucketName,
    // TODO timeoutSeconds, userPoolName, userPoolID, userPoolClientID, userPoolSecretKey, nullAttributeValue

    private static LambdaLogger logger;
    private static boolean ifDebug = Boolean.parseBoolean(System.getenv("ifDebug"));

    public static int workoutShortestTimeSectionInterval = Integer.parseInt(Optional.ofNullable(System.getenv
            ("workoutShortestTimeSectionInterval")).orElse("15"));

    // ID Creation stuff
    public static int idLength = Integer.parseInt(Optional.ofNullable(System.getenv("idLength")).orElse("14"));
    public static int numPrefix = Integer.parseInt(Optional.ofNullable(System.getenv("numPrefix")).orElse("2"));

    // Access Materials
    public static String databaseTableName = System.getenv("databaseTableName");
    public static String firebaseTokenTableName = System.getenv("firebaseTokenTableName");
    public static String messageTableName = System.getenv("messageTableName");
    public static String firebaseFunctionName = System.getenv("firebaseFunctionName");
    public static String storageBucketName = System.getenv("storageBucketName");

    // Potential usage????
    public static int timeoutSeconds = Integer.parseInt(Optional.ofNullable(System.getenv("timeoutSeconds")).orElse("10"));

    // Secret Information????
    public static String userPoolName = System.getenv("userPoolName");
    public static String userPoolID = System.getenv("userPoolID");
    public static String userPoolClientID = System.getenv("userPoolClientID");
    public static String userPoolSecretKey = System.getenv("userPoolSecretKey");
    public static String adminKey = System.getenv("adminKey");

    // Database Limits
    public static int userProfileImagePathsLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("userProfileImagePathsLimit")).orElse("10"));
    public static int postPicturePathsLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("postPicturePathsLimit")).orElse("10"));
    public static int postVideoPathsLimit = Integer.parseInt(Optional.ofNullable(System.getenv
            ("postVideoPathsLimit")).orElse("5"));

    // public static String nullAttributeValue = System.getenv("nullAttributeValue");

    public static void debugLog(String message) {
        if (ifDebug) {
            if (message != null) {
                Constants.logger.log(message + "\n");
            }
            else {
                Constants.logger.log("Tried to log a null message!\n");
            }
        }
    }

    public static void setLogger(LambdaLogger logger) {
        Constants.logger = logger;
    }
}
