package test.java.databaseOperations;

import org.junit.Test;

import main.java.databaseOperations.CheckHandler;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class CheckHandlerTest {
    @Test
    public void testCheckHandlerThrows() throws Exception {
        CheckHandler checkHandler = newItem -> {
            if (newItem == null) {
                return "error";
            }
            else {
                return null;
            }
        };
        assertEquals("error", checkHandler.isViable(null));
    }

    @Test
    public void testCheckHandlerNoThrow() throws Exception {
        CheckHandler checkHandler = newItem -> {
            if (newItem == null) {
                return null;
            }
            else {
                return "error";
            }
        };
        assertNull(checkHandler.isViable(null));
    }
}
