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

    Client(Item item) throws Exception {
        super(item);
        Set<String> friends = item.getStringSet("friends");
        if (friends != null) { this.friends = friends; }
        else { this.friends = new HashSet<>(); }
        Set<String> friendRequests = item.getStringSet("friend_requests");
        if (friendRequests != null) { this.friendRequests = friendRequests; }
        else { this.friendRequests = new HashSet<>(); }
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
        item.put("item_type", new AttributeValue("Client"));
        item.put("friends", null);
        item.put("friend_requests", null);
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
