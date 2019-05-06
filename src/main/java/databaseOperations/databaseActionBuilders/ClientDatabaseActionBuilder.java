package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Client;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;


/**
 * The Database Action Builder for the Client object, getting the {@link DatabaseAction} objects
 * that dictate the individual actions to do in the database for Clients.
 */
public class ClientDatabaseActionBuilder {
    final static private String itemType = "Client";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateClientRequest createClientRequest, Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Client.getEmptyItem();
        item.put("name", new AttributeValue(createClientRequest.name));
        item.put("email", new AttributeValue(createClientRequest.email));
        item.put("username", new AttributeValue(createClientRequest.username));
        if (createClientRequest.gender != null) { item.put("gender",
                new AttributeValue(createClientRequest.gender)); }
        if (createClientRequest.birthday != null) { item.put("birthday",
                new AttributeValue(createClientRequest.birthday)); }
        if (createClientRequest.bio != null) { item.put("bio",
                new AttributeValue(createClientRequest.bio)); }
        if (createClientRequest.stripeID != null) { item.put("stripeID",
                new AttributeValue(createClientRequest.stripeID)); }
        if (createClientRequest.federatedID != null) { item.put("federatedID",
                new AttributeValue(createClientRequest.federatedID)); }
        if (createClientRequest.enterpriseID != null) { item.put("enterpriseID",
                new AttributeValue(createClientRequest.enterpriseID)); }
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
                (Map<String, AttributeValue> createdItem, String id) -> {
                    return;
                }
        );
    }

    public static DatabaseAction updateAddTrainerFollowing(String id, String trainer) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "trainersFollowing", new AttributeValue(trainer), false, ADD);
    }

    public static DatabaseAction updateRemoveTrainerFollowing(String id, String trainer) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "trainersFollowing", new AttributeValue(trainer), false,
                DELETE);
    }

    public static DatabaseAction updateAddSubscription(String id, String subscription) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "subscriptions", new AttributeValue(subscription), false, ADD);
    }

    public static DatabaseAction updateRemoveSubscription(String id, String subscripion) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "subscriptions", new AttributeValue(subscripion), false,
                DELETE);
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
