package test.java.testing;

import org.junit.ClassRule;
import org.junit.Test;

import test.java.LocalDynamoDBCreationRule;

public class TestTableHelperTest {
    @ClassRule
    public static LocalDynamoDBCreationRule rule = new LocalDynamoDBCreationRule();

    @Test
    public void buildTable() {

    }
}
