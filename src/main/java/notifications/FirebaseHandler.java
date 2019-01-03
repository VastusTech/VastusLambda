package main.java.notifications;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import main.java.Logic.Constants;
import main.java.databaseOperations.DynamoDBHandler;

import java.io.InputStream;
import java.util.*;

public class FirebaseHandler {
    private Table firebaseTokenTable;
    private List<Message> messages;

    public FirebaseHandler() throws Exception {
        // Initialize the Firebase Client
        InputStream serviceAccount = getClass().getResourceAsStream("/firebaseSecretServiceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://testfirebaseproject-3c457.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);

        firebaseTokenTable = new Table(DynamoDBHandler.getInstance().client, Constants.firebaseTokenTableName);

        messages = new ArrayList<>();
    }

    public void addMessageForUser(String userID, Map<String, String> data) {
        for (String token : getTokens(userID)) {
            addMessage(token, data);
        }
    }

    private Set<String> getTokens(String userID) {
        Item item = firebaseTokenTable.getItem("id", userID);
        Set<String> tokens = item.getStringSet("tokens");
        String expires = item.getString("expires");

        // TODO Make sure that these tokens haven't expired?

        if (tokens == null) { tokens = new HashSet<String>(); }
        return tokens;
    }

    private void addMessage(String token, Map<String, String> data) {
        messages.add(buildMessage(token, data));
    }

    private  Message buildMessage(String token, Map<String, String> data) {
        // See documentation on defining a message payload.
        Message.Builder messageBuilder = Message.builder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            messageBuilder.putData(entry.getKey(), entry.getValue());
        }
        return messageBuilder.setToken(token).build();
    }

    public void sendMessages() throws FirebaseMessagingException {
        for (Message message : this.messages) {
            sendMessage(message);
        }
    }

    private void sendMessage(Message message) throws FirebaseMessagingException {
        // Send a message to the device corresponding to the provided registration token.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        Constants.debugLog("Successfully sent message: " + response);
    }
}
