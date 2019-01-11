package main.java.notifications;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import main.java.Logic.Constants;

public class NotificationHandler {
    private List<Notification> notifications;

    public NotificationHandler() {
        notifications = new ArrayList<>();
    }

    public void sendMessages() {
        LambdaInvokeHandler.getInstance().invokeLambda(Constants.ablyFunctionName, getNotificationsPayload());
    }

    public void addMessage(String channel, String type, JsonObjectBuilder payload) throws Exception {
        notifications.add(new Notification(channel, type, payload));
    }

    private String getNotificationsPayload() {
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Notification notification : notifications) {
            json.add(notification.createMessageJSON());
        }
        return Json.createObjectBuilder().add("messages", json).build().toString();
    }

    public enum NotificationType {
        inviteNotification,
        messageNotification
    }

    private class Notification {
        String channel;
        String type;
        JsonObjectBuilder payload;

        private Notification(String channel, String type, JsonObjectBuilder payload) {
            this.channel = channel;
            this.type = type;
            this.payload = payload;
        }

        JsonObjectBuilder createMessageJSON() {
            return Json.createObjectBuilder()
                    .add("channel", channel)
                    .add("type", type)
                    .add("payload", payload);
        }
    }
}
