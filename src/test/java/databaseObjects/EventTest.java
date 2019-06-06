package test.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import main.java.databaseObjects.Event;
import main.java.databaseObjects.TimeInterval;
import main.java.logic.TimeHelper;

public class EventTest {
    static Item getEventTestItem() throws Exception {
        return new Item()
                .withString("item_type", "Event")
                .withString("id", "EV0001")
                .withNumber("marker", 1)
                .withString("time_created", TimeHelper.nowString())
                .withString("title", "TITLE")
                .withString("description", "DESCRIPTION")
                .withString("owner", "OWNER")
                .withString("time", getTestTimeInterval().toString())
                .withString("address", "ADDRESS")
                .withStringSet("members", Sets.newSet("1", "2", "3"))
                .withStringSet("invitedMembers", Sets.newSet("1", "2", "3"))
                .withStringSet("memberRequests", Sets.newSet("1", "2", "3"))
                .withStringSet("receivedInvites", Sets.newSet("1", "2", "3"))
                .withString("capacity", "1000")
                .withString("ifCompleted", "false")
                .withString("access", "ACCESS")
                .withString("restriction", "RESTRICTION")
                .withString("challenge", "CHALLENGE")
                .withString("group", "GROUP")
                .withStringSet("tags", Sets.newSet("1", "2", "3"));
    }

    private static TimeInterval getTestTimeInterval() throws Exception {
        return new TimeInterval(TimeHelper.isoString(new DateTime(2000, 10, 5, 12, 0))
                + "_" + TimeHelper.isoString(new DateTime(2000, 10, 5, 13, 0)));
    }

    @Test
    public void test() {
        Event event;
    }
}
