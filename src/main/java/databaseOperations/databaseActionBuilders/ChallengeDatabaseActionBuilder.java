package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Challenge;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChallengeDatabaseActionBuilder {
    private static final String itemType = "Challenge";

    public static DatabaseAction create(CreateChallengeRequest createChallengeRequest) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Challenge.getEmptyItem();
        item.put("owner", new AttributeValue(createChallengeRequest.owner));
        item.put("time", new AttributeValue(createChallengeRequest.time));
        item.put("capacity", new AttributeValue(createChallengeRequest.capacity));
        item.put("address", new AttributeValue(createChallengeRequest.address));
        item.put("title", new AttributeValue(createChallengeRequest.title));
        item.put("goal", new AttributeValue(createChallengeRequest.goal));
        if (createChallengeRequest.description != null) { item.put("description", new AttributeValue(createChallengeRequest
                .description)); }
        if (createChallengeRequest.difficulty != null) { item.put("difficulty", new AttributeValue(createChallengeRequest
                .difficulty)); }
        if (createChallengeRequest.members != null) { item.put("members", new AttributeValue
                (Arrays.asList(createChallengeRequest.members))); }
        if (createChallengeRequest.access != null) { item.put("access", new AttributeValue(createChallengeRequest
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

    public static DatabaseAction updateGoal(String id, String goal) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "goal", new AttributeValue(goal), false, "PUT");
    }

    public static DatabaseAction updateAddMember(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "members", new AttributeValue(client), false, "ADD", new
                CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                // The capacity for the challenge must not be filled up yet.
                Challenge challenge = (Challenge) newObject;
                if (challenge.capacity > challenge.members.size()) {
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

    public static DatabaseAction updateWinner(String id, String winner) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "winner", new AttributeValue(winner), false, "PUT");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Challenge"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
