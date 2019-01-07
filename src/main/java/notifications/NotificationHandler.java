package main.java.notifications;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import main.java.Logic.Constants;
import main.java.databaseOperations.DynamoDBHandler;

public class NotificationHandler {
    // POJO that we define ourselves and convert to JSON
    private List<Message> messages;

    public NotificationHandler() {
        messages = new ArrayList<>();
    }

    public void sendMessages() {
        LambdaInvokeHandler.getInstance().invokeLambda(Constants.firebaseFunctionName, getMessagesPayload());
    }

    public void addMessage(String userID, String title, String body) throws Exception {
        for (String token : DynamoDBHandler.getInstance().getFirebaseTokens(userID)) {
            messages.add(new Message(token, title, body));
        }
    }

    private String getMessagesPayload() {
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Message message : messages) {
            json.add(message.createMessageJSON());
        }
        return Json.createObjectBuilder().add("messages", json).toString();
    }

    public enum NotificationType {
        inviteNotification,
        messageNotification
    }

    private class Message {
        String token;
//        String type;
        String title;
        String body;

        private Message(String token, String title, String body) {
            this.token = token;
//            this.type = type;
            this.title = title;
            this.body = body;
        }

        JsonObjectBuilder createMessageJSON() {
            return Json.createObjectBuilder()
                    .add("token", token)
                    .add("notification", Json.createObjectBuilder()
//                        .add("type", this.type)
                        .add("title", this.title)
                        .add("body", this.body)
                    );
        }
    }
}
