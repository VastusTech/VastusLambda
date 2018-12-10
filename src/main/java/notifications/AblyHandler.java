package main.java.notifications;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.Message;
import main.java.Logic.Constants;

import java.util.HashMap;
import java.util.Map;

public class AblyHandler {
    private AblyRealtime ably;
    private HashMap<String, Message> notifications;

    public AblyHandler() throws Exception {
        ably = new AblyRealtime(Constants.ablyAPIKey);
        notifications = new HashMap<>();
    }

    public void addNotification(String id, String title, Object notification) throws Exception {
        notifications.put(id, new Message(title, notification));
    }

    public void sendNotifications() throws Exception {
        for (Map.Entry<String, Message> entry: this.notifications.entrySet()) {
            String id = entry.getKey();
            Message notification = entry.getValue();

            Channel channel = ably.channels.get(id);
            channel.publish(notification);
        }
    }
}
