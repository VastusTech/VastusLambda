package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.ClientResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.*;

public class Client extends User {
    public Set<String> friends;
    public Set<String> friendRequests;
    public Set<String> challengesWon;
    public Set<String> scheduledEvents;
    public Set<String> completedEvents;
    public Set<String> ownedEvents;
    public Set<String> invitedEvents;

    Client(Item item) throws Exception {
        super(item);
        this.friends = item.getStringSet("friends");
        if (friends == null) { this.friends = new HashSet<>(); }
        this.friendRequests = item.getStringSet("friendRequests");
        if (friendRequests == null) { this.friendRequests = new HashSet<>(); }
        this.challengesWon = item.getStringSet("challengesWon");
        if (challengesWon == null) { this.challengesWon = new HashSet<>(); }
        this.scheduledEvents = item.getStringSet("scheduledEvents");
        if (scheduledEvents == null) { this.scheduledEvents = new HashSet<>(); }
        this.completedEvents = item.getStringSet("completedEvents");
        if (completedEvents == null) { this.completedEvents = new HashSet<>(); }
        this.ownedEvents = item.getStringSet("ownedEvents");
        if (ownedEvents == null) { this.ownedEvents = new HashSet<>(); }
        this.invitedEvents = item.getStringSet("invitedEvents");
        if (invitedEvents == null) { this.invitedEvents = new HashSet<>(); }
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Client"));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new ClientResponse(this);
    }

    // TODO Implement cache system here again?
    public static Client readClient(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Client"));
        key.put("id", new AttributeValue(id));
        return DynamoDBHandler.getInstance().readItem(key);
    }

    public static Client queryClient(String username) throws Exception {
        return DynamoDBHandler.getInstance().usernameQuery(username, "Client");
    }
}
