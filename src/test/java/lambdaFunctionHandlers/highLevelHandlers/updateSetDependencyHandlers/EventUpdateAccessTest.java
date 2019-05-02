package test.java.lambdaFunctionHandlers.highLevelHandlers.updateSetDependencyHandlers;

import org.junit.Before;
import org.junit.ClassRule;

import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

public class EventUpdateAccessTest {
    @ClassRule
    public static LocalDynamoDBCreationRule rule = new LocalDynamoDBCreationRule();

    @Before
    public void init() throws Exception {
        TestHelper.reinitTablesFromJSON("table1.json", "table1.json");;
    }

    // ===========================================================================================
    // ==                            EXPECTED SUCCESS TESTS                                     ==
    // ===========================================================================================

    // TODO

    // ===========================================================================================
    // ==                              EXPECTED ERROR TESTS                                     ==
    // ===========================================================================================

    // TODO

}
