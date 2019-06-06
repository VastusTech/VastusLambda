package test.java.lambdaFunctionHandlers.requestObjects;

import org.junit.Test;

import main.java.lambdaFunctionHandlers.requestObjects.CreateGymRequest;

import static test.java.lambdaFunctionHandlers.requestObjects.CreateObjectRequestTest.failsForEmptyStringsInRequest;

public class CreateGymRequestTest {
    @Test
    public void testEmptyStringsInRequest() throws Exception {
        failsForEmptyStringsInRequest(o -> new CreateGymRequest());
    }
}
