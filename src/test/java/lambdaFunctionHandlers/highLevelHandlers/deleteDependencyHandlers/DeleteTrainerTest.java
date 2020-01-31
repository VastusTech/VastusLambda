package test.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collections;

import main.java.databaseOperations.DatabaseActionCompiler;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateTrainer;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteTrainer;
import main.java.lambdaFunctionHandlers.requestObjects.CreateTrainerRequest;
import main.java.logic.Constants;
import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

import static junit.framework.TestCase.assertFalse;

public class DeleteTrainerTest {
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
        String id = DynamoDBHandler.getInstance().attemptTransaction(CreateTrainer.getCompilers(
                Constants.adminKey, new CreateTrainerRequest("Leo", null, null,
                        "EMAIL", "LB", null, null, null,
                        null, null, null, null,
                        null), 0
        ));
        DynamoDBHandler.getInstance().attemptTransaction(Collections.singletonList(new
                DatabaseActionCompiler(DeleteTrainer.getActions(Constants.adminKey, id))));
        assertFalse(DynamoDBHandler.getInstance().existsInDatabaseTable(id, "Trainer"));
    }

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    // TODO

}
