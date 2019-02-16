package test.java.notificationTests;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.json.Json;

import main.java.notifications.Notification;

import static org.junit.Assert.assertEquals;

public class NotificationTest {
    @Test
    public void testEmptyInit() {
        Notification notification = new Notification("channel", "type");
        Map<String, Object> expected = getJSONTemplate("channel", "type");
        assertNotificationEquals(expected, notification);
    }

    @Test
    public void testInitWithEmptyPayload() {
        Map<String, Object> payload = new HashMap<>();
        Notification notification = new Notification("channel", "type", payload);
        assertNotificationEquals(getJSONTemplate("channel", "type"), notification);
    }

    @Test
    public void testInitWithNonEmptyPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("test", "test");
        Notification notification = new Notification("channel", "type", payload);
        Map<String, Object> expected = getJSONTemplate("channel", "type");
        getNestedField("payload", expected).put("test", "test");
        assertNotificationEquals(expected, notification);
    }

    @Test
    public void testInitWithNestedPayload() {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> nestedPayload = new HashMap<>();
        nestedPayload.put("test", "test");
        payload.put("test", nestedPayload);
        Notification notification = new Notification("channel", "type", payload);
        Map<String, Object> expected = getJSONTemplate("channel", "type");
        getNestedField("payload", expected).put("test", nestedPayload);
        assertNotificationEquals(expected, notification);
    }

    @Test
    public void testAfterPayload() {

    }

    @Test
    public void testReplaceAllFields() {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> testFields = new HashMap<>();
        testFields.put("test1", "test");
        testFields.put("test2", "test");
        testFields.put("test3", "not-test");
        Set<String> testSet = new HashSet<>();
        testSet.add("not-test");
        testSet.add("test");
        testFields.put("test4", testSet);
        payload.put("test", testFields);
        Notification notification = new Notification("channel", "type", payload);
        notification.replaceFromPayloadField("test", "test", "definitely-test");
        Map<String, Object> expected = getJSONTemplate("channel", "type");
        Map<String, Object> expectedFields = new HashMap<>();
        expectedFields.put("test1", "definitely-test");
        expectedFields.put("test2", "definitely-test");
        expectedFields.put("test3", "not-test");
        Set<String> expectedSet = new HashSet<>();
        expectedSet.add("not-test");
        expectedSet.add("definitely-test");
        expectedFields.put("test4", expectedSet);
        getNestedField("payload", expected).put("test", expectedFields);
        assertNotificationEquals(expected, notification);
    }

    private void assertNotificationEquals(Map<String, Object> expected, Notification actual) {
        assertEquals(Json.createObjectBuilder(expected).build(), actual.createMessageJSON().build());
    }

    private Map<String, Object> getJSONTemplate(String channel, String type) {
        Map<String, Object> json = new HashMap<>();
        json.put("channel", channel);
        json.put("type", type);
        json.put("payload", new HashMap<String, Object>());
        return json;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getNestedField(String name, Map<String, Object> map) {
        return (Map<String, Object>)map.get(name);
    }
}
