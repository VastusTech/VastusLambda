package test.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers;

import org.junit.Before;
import org.junit.ClassRule;

import main.java.testing.TestHelper;
import test.java.LocalDynamoDBCreationRule;

// TODO REVISIT ONCE WE REIMPLEMENT THIS

public class CreateGymTest {
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
