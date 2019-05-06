package main.java.testing;

import main.java.databaseOperations.DynamoDBHandler;

/**
 * Class to keep track of testing values and to help initialize the environment for full testing.
 */
public class TestHelper {
    private static boolean ifTesting = false;
    private static String port = "8000";

    /**
     * Initializes database tables for the {@link DynamoDBHandler} instance as DynamoDB Local tables
     * from local JSON files.
     *
     * @param databaseJsonName The JSON file to indicate the value of the Database Table.
     * @param messagesTableJsonName The JSON file to indicate the value of the Messages Table.
     * @throws Exception If anything goes wrong in the initialization.
     */
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
