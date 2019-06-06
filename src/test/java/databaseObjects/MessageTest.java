package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import main.java.databaseObjects.Message;
import main.java.logic.TimeHelper;

public class MessageTest {
    static Item getMessageTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Message")
                .withString("id", "ME0001")
                .withString("board", "CL0001_CL0002")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("from", "FROM")
                .withString("type", "TYPE")
                .withString("name", "NAME")
                .withString("profileImagePath", "PROFILEIMAGEPATH")
                .withString("message", "MESSAGE")
                .withStringSet("lastSeenFor", Sets.newSet("1", "2", "3"));
    }

    @Test
    public void test() {
        Message message;
    }
}
