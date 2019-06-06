package test.java.lambdaFunctionHandlers.requestObjects;

import org.junit.Test;

import main.java.lambdaFunctionHandlers.requestObjects.CreateTrainerRequest;

import static test.java.lambdaFunctionHandlers.requestObjects.CreateObjectRequestTest.failsForEmptyStringsInRequest;

public class CreateTrainerRequestTest {
    @Test
    public void testEmptyStringsInRequest() throws Exception {
        failsForEmptyStringsInRequest(o -> new CreateTrainerRequest());
    }
}
