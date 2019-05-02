package main.java.testing;

import main.java.databaseOperations.DynamoDBHandler;

public class TestHelper {
    private static boolean ifTesting = false;
    private static String port = "8000";

    public static void reinitTablesFromJSON(String databaseJsonName, String messagesTableJsonName)
            throws Exception {
        DynamoDBHandler.getInstance().setDatabaseTable(TestTableHelper.getInstance().
                reinitTestDatabaseTable(DynamoDBHandler.getInstance().client,
                        "src/test/resources/databaseTestTables/" + databaseJsonName));
        DynamoDBHandler.getInstance().setMessageTable(TestTableHelper.getInstance().
                reinitTestMessagesTable(DynamoDBHandler.getInstance().client,
                        "src/test/resources/messagesTestTables/" + messagesTableJsonName));
    }

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
}
