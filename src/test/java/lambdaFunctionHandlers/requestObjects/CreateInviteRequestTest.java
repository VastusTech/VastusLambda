package test.java.lambdaFunctionHandlers.requestObjects;

import org.junit.Test;

import main.java.lambdaFunctionHandlers.requestObjects.CreateInviteRequest;

import static test.java.lambdaFunctionHandlers.requestObjects.CreateObjectRequestTest.failsForEmptyStringsInRequest;

public class CreateInviteRequestTest {
    @Test
    public void testEmptyStringsInRequest() throws Exception {
        failsForEmptyStringsInRequest(o -> new CreateInviteRequest());
    }
}
