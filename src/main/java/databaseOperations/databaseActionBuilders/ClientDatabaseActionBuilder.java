package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Client;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClientDatabaseActionBuilder {
    final static private String itemType = "Client";

    public static DatabaseAction create(CreateClientRequest createClientRequest) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Client.getEmptyItem();
        item.put("name", new AttributeValue(createClientRequest.name));
        item.put("gender", new AttributeValue(createClientRequest.gender));
        item.put("birthday", new AttributeValue(createClientRequest.birthday));
        item.put("email", new AttributeValue(createClientRequest.email));
        item.put("username", new AttributeValue(createClientRequest.username));
        if (createClientRequest.bio != null) { item.put("bio", new AttributeValue(createClientRequest.bio)); }
        return new CreateDatabaseAction(item);
    }

    public static DatabaseAction updateAddTrainerFollowing(String id, String trainer) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "trainersFollowing", new AttributeValue(trainer), false, "ADD");
    }

    public static DatabaseAction updateRemoveTrainerFollowing(String id, String trainer) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "trainersFollowing", new AttributeValue(trainer), false,
                "DELETE");
    }

    public static DatabaseAction updateAddSubscription(String id, String subscription) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "subscriptions", new AttributeValue(subscription), false, "ADD");
    }

    public static DatabaseAction updateRemoveSubscription(String id, String subscripion) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "subscriptions", new AttributeValue(subscripion), false,
                "DELETE");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
