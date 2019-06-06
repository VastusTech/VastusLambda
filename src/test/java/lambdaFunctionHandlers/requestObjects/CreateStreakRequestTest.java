package test.java.lambdaFunctionHandlers.requestObjects;

import org.junit.Test;

import main.java.lambdaFunctionHandlers.requestObjects.CreateStreakRequest;

import static test.java.lambdaFunctionHandlers.requestObjects.CreateObjectRequestTest.failsForEmptyStringsInRequest;

public class CreateStreakRequestTest {
    @Test
    public void testEmptyStringsInRequest() throws Exception {
        failsForEmptyStringsInRequest(o -> new CreateStreakRequest());
    }
}
