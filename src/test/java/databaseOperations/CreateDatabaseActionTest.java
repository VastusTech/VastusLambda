package test.java.databaseOperations;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.UpdateWithIDHandler;

import static org.junit.Assert.assertEquals;

// TODO FINISH
public class CreateDatabaseActionTest {
    private UpdateWithIDHandler handler = (item, id) -> {};

    @Test
    public void createDatabaseActionEqualityBare() {
        assertEquals(
                new CreateDatabaseAction("ITEMTYPE", new HashMap<>(), new HashMap<>(),
                        null),
                new CreateDatabaseAction("ITEMTYPE", new HashMap<>(), new HashMap<>(),
                        null));
    }
    @Test
    public void createDatabaseActionEquality() {
        assertEquals(
                new CreateDatabaseAction("ITEMTYPE", new HashMap<>(), new HashMap<>(),
                        handler),
                new CreateDatabaseAction("ITEMTYPE", new HashMap<>(), new HashMap<>(),
                        handler));
    }
    @Test
    public void createDatabaseActionEqualityItem() {
        Map<String, AttributeValue> expectedItem = new HashMap<>();
        expectedItem.put("name", new AttributeValue("value"));
        Map<String, AttributeValue> actualItem = new HashMap<>();
        expectedItem.put("name", new AttributeValue("value"));
        assertEquals(
                new CreateDatabaseAction("ITEMTYPE", expectedItem, new HashMap<>(),
                        handler),
                new CreateDatabaseAction("ITEMTYPE", actualItem, new HashMap<>(),
                        handler));
    }

    @Test
    public void createDatabaseActionInequality() {

    }
}
