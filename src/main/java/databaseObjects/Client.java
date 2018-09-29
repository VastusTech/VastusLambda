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
    public int challengesWon;
    public Set<String> scheduledParties;
    public Set<String> scheduledChallenges;
    public Set<String> completedParties;
    public Set<String> completedChallenges;

    Client(Item item) throws Exception {
        super(item);
        this.friends = item.getStringSet("friends");
        if (friends == null) { this.friends = new HashSet<>(); }
        this.friendRequests = item.getStringSet("friend_requests");
        if (friendRequests == null) { this.friendRequests = new HashSet<>(); }
        this.scheduledParties = item.getStringSet("scheduled_parties");
        if (scheduledParties == null) { this.scheduledParties = new HashSet<>(); }
        this.scheduledChallenges = item.getStringSet("scheduled_challenges");
        if (scheduledChallenges == null) { this.scheduledChallenges = new HashSet<>(); }
        this.completedParties = item.getStringSet("completed_parties");
        if (completedParties == null) { this.completedParties = new HashSet<>(); }
        this.completedChallenges = item.getStringSet("completed_challenges");
        if (completedChallenges == null) { this.completedChallenges = new HashSet<>(); }
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
