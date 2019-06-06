package test.java.lambdaFunctionHandlers.requestObjects;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.function.Function;

import main.java.lambdaFunctionHandlers.highLevelHandlers.createDependencyHandlers.CreateClient;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;
import main.java.lambdaFunctionHandlers.requestObjects.CreateObjectRequest;

import static test.java.lambdaFunctionHandlers.requestObjects.CreateObjectRequestTest.failsForEmptyStringsInRequest;

public class CreateClientRequestTest {
    @Test
    public void testEmptyStringsInRequest() throws Exception {
        failsForEmptyStringsInRequest(o -> new CreateClientRequest());
    }
}
