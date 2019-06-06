package test.java.lambdaFunctionHandlers.requestObjects;

import org.junit.Test;

import main.java.lambdaFunctionHandlers.requestObjects.CreateMessageRequest;

import static test.java.lambdaFunctionHandlers.requestObjects.CreateObjectRequestTest.failsForEmptyStringsInRequest;

public class CreateMessageRequestTest {
    @Test
    public void testEmptyStringsInRequest() throws Exception {
        failsForEmptyStringsInRequest(o -> new CreateMessageRequest());
    }
}
