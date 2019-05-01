package test.java;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;

import java.util.Scanner;

import main.java.databaseOperations.DynamoDBHandler;
import main.java.testing.TestHelper;

public class RunLocalDatabase {
    public static void main(String[] args) throws Exception {
        TestHelper.setIfTesting(true);
        DynamoDBProxyServer server;
        System.setProperty("sqlite4java.library.path", "native-libs");
        String port = "8000";
        server = ServerRunner.createServerFromCommandLineArgs(
                new String[]{"-inMemory", "-port", port});
        server.start();
        DynamoDBHandler.getInstance();
        System.out.println("The server has successfully started at https://localhost:8000/");
        System.out.println("Press enter to close the server...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        server.stop();
    }
}
