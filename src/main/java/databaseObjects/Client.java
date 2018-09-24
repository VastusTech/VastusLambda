package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.ClientResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.*;

public class Client extends User {
    public Set<String> friends;
    public Set<String> friendRequests;

    Client(Map<String, AttributeValue> item) throws Exception {
        super(item);
        this.friends = new HashSet<>(item.get("friends").getSS());
        this.friendRequests = new HashSet<>(item.get("friend_requests").getSS());
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = User.getEmptyItem();
//        item.put("friends", new AttributeValue(new ArrayList<>()));
//        item.put("friend_requests", new AttributeValue(new ArrayList<>()));
        item.put("item_type", new AttributeValue("Client"));
        item.put("friends", null);
        item.put("friend_requests", null);
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new ClientResponse(this);
    }

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
