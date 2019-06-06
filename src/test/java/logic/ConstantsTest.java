package test.java.logic;

import org.junit.Test;

import main.java.logic.Constants;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ConstantsTest {
    @Test
    public void testDefaultValues() {
        assertFalse(Constants.ifDevelopment);
        assertFalse(Constants.ifDebug);
        assertEquals("admin", Constants.adminKey);
        assertEquals("Classics", Constants.databaseTableName);
        assertEquals("Messages", Constants.messageTableName);
        assertEquals("ClassicsTest", Constants.developmentDatabaseTableName);
        assertEquals("MessagesTest", Constants.developmentMessageTableName);
        assertEquals("VastusAblyLambdaFunction", Constants.ablyFunctionName);
        assertEquals(15, Constants.workoutShortestTimeSectionInterval);
        assertEquals(2, Constants.numPrefix);
//        assertEquals(2, Constants.);
    }
}
