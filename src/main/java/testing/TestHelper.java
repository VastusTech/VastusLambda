package main.java.testing;

public class TestHelper {
    private static boolean ifTesting = false;
    private static String port = "8000";
    private static String databaseTableJsonPath = "src/test/resources/databaseTestTables/table1.json";
    private static String messagesTableJsonPath = "src/test/resources/messagesTestTables/table1.json";

    public static boolean getIfTesting() {
        return TestHelper.ifTesting;
    }

    public synchronized static void setIfTesting(boolean ifTesting) {
        TestHelper.ifTesting = ifTesting;
    }

    public static String getPort() {
        return TestHelper.port;
    }

    public synchronized static void setPort(String port) {
        TestHelper.port = port;
    }

    public static String getDatabaseTableJsonPath() {
        return databaseTableJsonPath;
    }

    public static void setDatabaseTableJsonPath(String databaseTableJsonPath) {
        TestHelper.databaseTableJsonPath = databaseTableJsonPath;
    }

    public static String getMessagesTableJsonPath() {
        return messagesTableJsonPath;
    }

    public static void setMessagesTableJsonPath(String messagesTableJsonPath) {
        TestHelper.messagesTableJsonPath = messagesTableJsonPath;
    }
}
