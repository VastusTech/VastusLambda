package test.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import main.java.databaseObjects.Client;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateClient;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CreateClientTest {
    @ClassRule
    public static LocalDynamoDBCreationRule rule = new LocalDynamoDBCreationRule();

    @Before
    public void init() throws Exception {
        TestHelper.reinitTablesFromJSON("table1.json", "table1.json");;
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void createMinimumClient() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        "NAME", null,
                        null, "EMAIL",
                        "USERNAME", null,
                        null, null, null
                ), 0
        ));
        Client client = Client.readClient(id);
        assertEquals("NAME", client.name);
        assertNull(null, client.gender);
        assertNull(null, client.birthday);
        assertEquals("EMAIL", client.email);
        assertEquals("USERNAME", client.username);
        assertNull(null, client.bio);
        assertNull(null, client.stripeID);
        assertNull(null, client.federatedID);
    }

    @Test
    public void createFederatedClient() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        "NAME", null,
                        null, "EMAIL",
                        "USERNAME", null,
                        null, "FEDERATEDID", null
                ), 0
        ));
        Client client = Client.readClient(id);
        assertEquals("NAME", client.name);
        assertNull(client.gender);
        assertNull(client.birthday);
        assertEquals("EMAIL", client.email);
        assertEquals("USERNAME", client.username);
        assertNull(client.bio);
        assertNull(client.stripeID);
        assertEquals("FEDERATEDID", client.federatedID);
    }

    @Test
    public void createExtraInfoClient() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        "NAME", "GENDER",
                        "1998-09-05", "EMAIL",
                        "USERNAME", "BIO",
                        null, null, null
                ), 0
        ));
        Client client = Client.readClient(id);
        assertEquals("NAME", client.name);
        assertEquals("GENDER", client.gender);
        assertEquals("1998-09-05", client.birthday);
        assertEquals("EMAIL", client.email);
        assertEquals("USERNAME", client.username);
        assertEquals("BIO", client.bio);
        assertNull(client.stripeID);
        assertNull(client.federatedID);
    }

    @Test
    public void createExtraInfoFederatedClient() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        "NAME", "GENDER",
                        "1998-09-05", "EMAIL",
                        "USERNAME", "BIO",
                        null, "FEDERATEDID", null
                ), 0
        ));
        Client client = Client.readClient(id);
        assertEquals("NAME", client.name);
        assertEquals("GENDER", client.gender);
        assertEquals("1998-09-05", client.birthday);
        assertEquals("EMAIL", client.email);
        assertEquals("USERNAME", client.username);
        assertEquals("BIO", client.bio);
        assertNull(client.stripeID);
        assertEquals("FEDERATEDID", client.federatedID);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = Exception.class)
    public void failNoName() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        null, null,
                        null, "EMAIL",
                        "USERNAME", null,
                        null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoEmail() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        "NAME", null,
                        null, null,
                        "USERNAME", null,
                        null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failNoUsername() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        "NAME", null,
                        null, "EMAIL",
                        null, null,
                        null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failBadBirthday() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        "NAME", null,
                        "BAD-BIRTHDAY", "EMAIL",
                        "USERNAME", null,
                        null, null, null
                ), 0
        ));
    }

    @Test(expected = Exception.class)
    public void failRepeatUsername() throws Exception {
        List<DatabaseActionCompiler> compilers = new ArrayList<>();
        compilers.addAll(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        "NAME", null,
                        null, "EMAIL",
                        "USERNAME", null,
                        null, null, null
                ), 0));
        compilers.addAll(CreateClient.getCompilers(
                Constants.adminKey, new CreateClientRequest(
                        "NAME", null,
                        null, "EMAIL",
                        "USERNAME", null,
                        null, null, null
                ), 0));
        DynamoDBHandler.getInstance().attemptTransaction(compilers);
    }
}
