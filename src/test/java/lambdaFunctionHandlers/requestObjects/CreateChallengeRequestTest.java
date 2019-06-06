package test.java.lambdaFunctionHandlers.requestObjects;

import org.junit.Test;

import java.lang.reflect.Field;

import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;

import static test.java.lambdaFunctionHandlers.requestObjects.CreateObjectRequestTest.failsForEmptyStringsInRequest;

public class CreateChallengeRequestTest {
    @Test
    public void testEmptyStringsInRequest() throws Exception {
        failsForEmptyStringsInRequest(o -> new CreateChallengeRequest());
    }
}
