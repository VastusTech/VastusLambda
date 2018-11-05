package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.Party;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePartyRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PartyDatabaseActionBuilder {
    private static final String itemType = "Party";

    public static DatabaseAction create(CreatePartyRequest createPartyRequest) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Party.getEmptyItem();
        item.put("owner", new AttributeValue(createPartyRequest.owner));
        item.put("time", new AttributeValue(createPartyRequest.time));
        item.put("capacity", new AttributeValue(createPartyRequest.capacity));
        item.put("address", new AttributeValue(createPartyRequest.address));
        item.put("title", new AttributeValue(createPartyRequest.title));
        if (createPartyRequest.description != null) { item.put("description", new AttributeValue(createPartyRequest
                .description)); }
        if (createPartyRequest.members != null) { item.put("members", new AttributeValue(Arrays.asList(createPartyRequest
                .members))); }
        if (createPartyRequest.access != null) { item.put("access", new AttributeValue(createPartyRequest
                .access)); }
        return new CreateDatabaseAction(item);
    }

    public static DatabaseAction updateTitle(String id, String title) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "title", new AttributeValue(title), false, "PUT");
    }

    public static DatabaseAction updateDescription(String id, String description) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "description", new AttributeValue(description), false, "PUT");
    }

    public static DatabaseAction updateAddress(String id, String address) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "address", new AttributeValue(address), false, "PUT");
    }

    public static DatabaseAction updateAccess(String id, String access) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "access", new AttributeValue(access), false, "PUT");
    }

    public static DatabaseAction updateAddMember(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "members", new AttributeValue(client), false, "ADD", new CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                // The capacity for the workout must not be filled up yet.
                Party party = (Party)newObject;
                if (party.capacity > party.members.size()) {
                    return null;
                }
                else {
                    return "That party is already filled up!";
                }
            }
        });
    }

    public static DatabaseAction updateRemoveMember(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "members", new AttributeValue(client), false, "DELETE");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Party"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
