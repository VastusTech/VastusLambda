package test.java.lambdaFunctionHandlers;

import org.junit.ClassRule;

import test.java.LocalDynamoDBCreationRule;

public class LambdaRequestTest {
    @ClassRule
    public static LocalDynamoDBCreationRule dynamoDB = new LocalDynamoDBCreationRule();

    public void test() {

    }
}
