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
        item.put("ownerID", new AttributeValue(createChallengeRequest.ownerID));
        item.put("time", new AttributeValue(createChallengeRequest.time));
        item.put("capacity", new AttributeValue(createChallengeRequest.capacity));
        item.put("address", new AttributeValue(createChallengeRequest.address));
        item.put("title", new AttributeValue(createChallengeRequest.title));
        item.put("goal", new AttributeValue(createChallengeRequest.goal));
        if (createChallengeRequest.description != null) { item.put("description", new AttributeValue(createChallengeRequest
                .description)); }
        if (createChallengeRequest.difficulty != null) { item.put("difficulty", new AttributeValue(createChallengeRequest
                .difficulty)); }
        if (createChallengeRequest.memberIDs != null) { item.put("description", new AttributeValue
                (Arrays.asList(createChallengeRequest.memberIDs))); }
        if (createChallengeRequest.access != null) { item.put("access", new AttributeValue(createChallengeRequest
                .access)); }
        return new CreateDatabaseAction(item);
    }

    public static DatabaseAction updateAddMemberID(String id, String clientID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "memberIDs", new AttributeValue(clientID), false, "ADD", new
                CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                // The capacity for the challenge must not be filled up yet.
                Challenge challenge = (Challenge) newObject;
                if (challenge.capacity > challenge.memberIDs.size()) {
                    return null;
                }
                else {
                    return "That challenge is already filled up!";
                }
            }
        });
    }

    public static DatabaseAction updateRemoveMemberID(String id, String clientID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "memberIDs", new AttributeValue(clientID), false, "REMOVE");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Challenge"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
