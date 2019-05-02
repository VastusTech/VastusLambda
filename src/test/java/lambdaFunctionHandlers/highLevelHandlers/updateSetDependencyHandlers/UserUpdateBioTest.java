package test.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collections;

import main.java.databaseObjects.Client;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers.UserUpdateBio;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserUpdateBioTest {
    @ClassRule
    public static LocalDynamoDBCreationRule rule = new LocalDynamoDBCreationRule();

    @Before
    public void init() throws Exception {
        TestHelper.reinitTablesFromJSON("table1.json", "table1.json");
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    @Test
    public void testUpdateBio() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(Collections.singletonList(new
                DatabaseActionCompiler(UserUpdateBio.getActions(
                        Constants.adminKey, "CL0001", "Client", "BIO"
        ))));
        assertEquals("BIO", Client.readClient("CL0001").bio);
    }

    @Test
    public void testUpdateNullBio() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(Collections.singletonList(new
                DatabaseActionCompiler(UserUpdateBio.getActions(
                Constants.adminKey, "CL0001", "Client", "BIO"
        ))));
        assertEquals("BIO", Client.readClient("CL0001").bio);
        DynamoDBHandler.getInstance().attemptTransaction(Collections.singletonList(new
                DatabaseActionCompiler(UserUpdateBio.getActions(
                Constants.adminKey, "CL0001", "Client", null
        ))));
        assertNull(Client.readClient("CL0001").bio);
    }

    @Test
    public void testUpdateBioAsUser() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(Collections.singletonList(new
                DatabaseActionCompiler(UserUpdateBio.getActions(
                "CL0001", "CL0001", "Client", "BIO"
        ))));
        assertEquals("BIO", Client.readClient("CL0001").bio);
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    @Test(expected = Exception.class)
    public void failUpdateBioNotAsUser() throws Exception {
        DynamoDBHandler.getInstance().attemptTransaction(Collections.singletonList(new
                DatabaseActionCompiler(UserUpdateBio.getActions(
                "CL0005", "CL0001", "Client", "BIO"
        ))));
    }

}
