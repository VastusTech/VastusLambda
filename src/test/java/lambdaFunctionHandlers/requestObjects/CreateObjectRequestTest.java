package test.java.lambdaFunctionHandlers.requestObjects;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.function.Function;

import main.java.databaseOperations.exceptions.ExceedsDatabaseLimitException;
import main.java.lambdaFunctionHandlers.requestObjects.CreateObjectRequest;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class CreateObjectRequestTest {
    @Test
    public void testEmptyStringFirst() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.hasEmptyString("", "A", "B"));
    }
    @Test
    public void testEmptyStringMiddle() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.hasEmptyString("A", "", "B"));
    }
    @Test
    public void testEmptyStringLast() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.hasEmptyString("", "A", "B"));
    }
    @Test
    public void testNoEmptyString() throws ExceedsDatabaseLimitException {
        assertFalse(CreateObjectRequest.hasEmptyString("A", "B", "C"));
    }
    @Test
    public void testEmptyStringArrayFirstFirst() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"", "B", "C"},
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", "C"}
        ));
    }
    @Test
    public void testEmptyStringArrayFirstMiddle() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"A", "", "C"},
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", "C"}
        ));
    }
    @Test
    public void testEmptyStringArrayFirstLast() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"A", "B", ""},
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", "C"}
        ));
    }
    @Test
    public void testEmptyStringArrayMiddleFirst() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"A", "B", "C"},
                new String[]{"", "B", "C"},
                new String[]{"A", "B", "C"}
        ));
    }
    @Test
    public void testEmptyStringArrayMiddleMiddle() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"A", "B", "C"},
                new String[]{"A", "", "C"},
                new String[]{"A", "B", "C"}
        ));
    }
    @Test
    public void testEmptyStringArrayMiddleLast() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", ""},
                new String[]{"A", "B", "C"}
        ));
    }
    @Test
    public void testEmptyStringArrayLastFirst() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", "C"},
                new String[]{"", "B", "C"}
        ));
    }
    @Test
    public void testEmptyStringArrayLastMiddle() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", "C"},
                new String[]{"A", "", "C"}
        ));
    }
    @Test
    public void testEmptyStringArrayLastLast() throws ExceedsDatabaseLimitException {
        assertTrue(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", ""}
        ));
    }
    @Test
    public void testNoEmptyStringArray() throws ExceedsDatabaseLimitException {
        assertFalse(CreateObjectRequest.arrayHasEmptyString(
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", "C"},
                new String[]{"A", "B", "C"}
        ));
    }
    public static void failsForEmptyStringsInRequest(Function<Object, CreateObjectRequest>
                                                             createRequest) throws Exception {
        CreateObjectRequest request = createRequest.apply(null);
        Class<?> requestClass = request.getClass();
        for (Field field : requestClass.getFields()) {
            CreateObjectRequest testRequest = createRequest.apply(null);
            if (field.getType() == String.class) {
                field.set(testRequest, "");
            }
            else if (field.getType() == String[].class) {
                field.set(testRequest, new String[]{"not_empty", ""});
            }
            else {
                throw new Exception("POJO Class has invalid field type! Class = " + field.getType());
            }
            if (!testRequest.ifHasEmptyString()) {
                throw new AssertionError("Request class: " + testRequest.getClass().getName()
                        + ", doesn't check for empty on variable: " + field.getName());
            }
        }
    }
}
