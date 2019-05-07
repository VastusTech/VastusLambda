package test.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collections;

import main.java.databaseObjects.Client;
import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateGroup;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteGroup;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGroupRequest;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

import static junit.framework.TestCase.assertFalse;

public class DeleteGroupTest {
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
    public void testCreateThenDeleteGroup() throws Exception {
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateGroup.getCompilers(
                "CL0001", new CreateGroupRequest(
                        "TITLE", "DESCRIPTION", "private", null,
                        null, new String[]{"CL0001"}, null, null, null
                ), 0
        ));
        DynamoDBHandler.getInstance().attemptTransaction(Collections.singletonList(new
                DatabaseActionCompiler(DeleteGroup.getActions(Constants.adminKey, id))));
        Client client = Client.readClient("CL0001");
        assertFalse(client.groups.contains(id));
        assertFalse(client.ownedGroups.contains(id));
        assertFalse(DynamoDBHandler.getInstance().existsInDatabaseTable(id, "Group"));
    }

    // TODO
    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    // TODO

}
