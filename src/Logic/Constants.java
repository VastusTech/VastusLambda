package Logic;

public class Constants {
    // TODO THIS GETS IT FROM THE ENVIRONMENTAL VARIABLES FROM AWS LAMBDA (with encryption ;))
    // TODO PUT THESE: workoutShortestTimeSectionInterval, idLength, numPrefix, databaseTableName, storageBucketName,
    // TODO timeoutSeconds, userPoolName, userPoolID, userPoolClientID, userPoolSecretKey
    public static int workoutShortestTimeSectionInterval = Integer.parseInt(System.getenv
            ("workoutShortestTimeSectionInterval"));

    // ID Creation stuff
    public static int idLength = Integer.parseInt(System.getenv("idLength"));
    public static int numPrefix = Integer.parseInt(System.getenv("numPrefix"));

    // Access Materials
    public static String databaseTableName = System.getenv("databaseTableName");
    public static String storageBucketName = System.getenv("storageBucketName");

    // Potential usage????
    public static int timeoutSeconds = Integer.parseInt(System.getenv("timeoutSeconds"));

    // Secret Information????
    public static String userPoolName = System.getenv("userPoolName");
    public static String userPoolID = System.getenv("userPoolID");
    public static String userPoolClientID = System.getenv("userPoolClientID");
    public static String userPoolSecretKey = System.getenv("userPoolSecretKey");
}
