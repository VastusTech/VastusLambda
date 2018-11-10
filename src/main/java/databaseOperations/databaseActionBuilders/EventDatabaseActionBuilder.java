package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.Event;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateEventRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EventDatabaseActionBuilder {
    private static final String itemType = "Event";

    public static DatabaseAction create(CreateEventRequest createEventRequest) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Event.getEmptyItem();
        item.put("owner", new AttributeValue(createEventRequest.owner));
        item.put("time", new AttributeValue(createEventRequest.time));
        item.put("capacity", new AttributeValue(createEventRequest.capacity));
        item.put("address", new AttributeValue(createEventRequest.address));
        item.put("title", new AttributeValue(createEventRequest.title));
        item.put("ifChallenge", new AttributeValue(createEventRequest.ifChallenge));
        if (createEventRequest.goal != null) { item.put("goal", new AttributeValue(createEventRequest.goal)); }
        if (createEventRequest.description != null) { item.put("description", new AttributeValue(createEventRequest
                .description)); }
        if (createEventRequest.difficulty != null) { item.put("difficulty", new AttributeValue(createEventRequest
                .difficulty)); }
        if (createEventRequest.members != null) { item.put("members", new AttributeValue
                (Arrays.asList(createEventRequest.members))); }
        if (createEventRequest.access != null) { item.put("access", new AttributeValue(createEventRequest
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

    public static DatabaseAction updateIfChallenge(String id, String ifChallenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "ifChallenge", new AttributeValue(ifChallenge), false, "PUT");
    }

    public static DatabaseAction updateGoal(String id, String goal) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "goal", new AttributeValue(goal), false, "PUT");
    }

    public static DatabaseAction updateDifficulty(String id, String difficulty) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "difficulty", new AttributeValue(difficulty), false, "PUT");
    }

    public static DatabaseAction updateAccess(String id, String access) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "access", new AttributeValue(access), false, "PUT");
    }

    public static DatabaseAction updateAddMember(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "members", new AttributeValue(client), false, "ADD", new
                CheckHandler() {
                    @Override
                    public String isViable(DatabaseObject newObject) throws Exception {
                        // The capacity for the challenge must not be filled up yet.
                        Event event = (Event) newObject;
                        if (event.capacity > event.members.size()) {
                            return null;
                        }
                        else {
                            return "That challenge is already filled up!";
                        }
                    }
                });
    }

    public static DatabaseAction updateRemoveMember(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "members", new AttributeValue(client), false, "DELETE");
    }

    public static DatabaseAction updateAddInvitedMember(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "invitedMembers", new AttributeValue(client), false, "ADD");
    }

    public static DatabaseAction updateRemoveInvitedMember(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "invitedMembers", new AttributeValue(client), false, "DELETE");
    }

    public static DatabaseAction updateWinner(String id, String winner) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "winner", new AttributeValue(winner), false, "PUT");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}

