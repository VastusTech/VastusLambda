package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.Logic.ItemType;
import main.java.databaseObjects.*;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateChallengeRequest;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChallengeDatabaseActionBuilder {
    private static final String itemType = "Challenge";

    public static DatabaseAction create(CreateChallengeRequest createChallengeRequest) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Challenge.getEmptyItem();
        item.put("owner", new AttributeValue(createChallengeRequest.owner));
        item.put("endTime", new AttributeValue(createChallengeRequest.endTime));
        item.put("capacity", new AttributeValue(createChallengeRequest.capacity));
        item.put("title", new AttributeValue(createChallengeRequest.title));
        item.put("goal", new AttributeValue(createChallengeRequest.goal));
        if (createChallengeRequest.prize != null) { item.put("prize", new AttributeValue(createChallengeRequest
                .prize)); }
        if (createChallengeRequest.description != null) { item.put("description", new AttributeValue(createChallengeRequest
                .description)); }
        if (createChallengeRequest.difficulty != null) { item.put("difficulty", new AttributeValue
                (createChallengeRequest.difficulty)); }
        if (createChallengeRequest.members != null) { item.put("members", new AttributeValue
                (Arrays.asList(createChallengeRequest.members))); }
        if (createChallengeRequest.access != null) { item.put("access", new AttributeValue(createChallengeRequest
                .access)); }
        if (createChallengeRequest.restriction != null) { item.put("restriction", new AttributeValue(createChallengeRequest
                .restriction)); }
        if (createChallengeRequest.tags != null) { item.put("tags", new AttributeValue
                (Arrays.asList(createChallengeRequest.tags))); }
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

    public static DatabaseAction updateIfCompleted(String id, String ifCompleted) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "ifCompleted", new AttributeValue(ifCompleted), false, "PUT");
    }

    public static DatabaseAction updateGoal(String id, String goal) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "goal", new AttributeValue(goal), false, "PUT");
    }

    public static DatabaseAction updateDifficulty(String id, String difficulty) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "difficulty", new AttributeValue(difficulty), false, "PUT");
    }

    public static DatabaseAction updateEndTime(String id, String endTime) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "endTime", new AttributeValue(endTime), false, "PUT");
    }

    public static DatabaseAction updatePrize(String id, String prize) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "prize", new AttributeValue(prize), false, "PUT");
    }

    public static DatabaseAction updateAccess(String id, String access) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "access", new AttributeValue(access), false, "PUT");
    }

    public static DatabaseAction updateRestriction(String id, String restriction) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "restriction", new AttributeValue(restriction), false, "PUT");
    }

    public static DatabaseAction updateAddMember(String id, String user, boolean ifAccepting) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "members", new AttributeValue(user), false, "ADD", new
                CheckHandler() {
                    @Override
                    public String isViable(DatabaseObject newObject) throws Exception {
                        // The capacity for the challenge must not be filled up yet.
                        Challenge challenge = (Challenge) newObject;
                        if (challenge.ifCompleted) {
                            return "That challenge has already been completed!";
                        }
                        if (challenge.capacity > challenge.members.size()) {
                            // Check to see if we are accepting a member request
                            if (ifAccepting) {
                                if (challenge.memberRequests.contains(user)) {
                                    return null;
                                }
                                else {
                                    return "You need to have a member request in order to accept it!";
                                }
                            }
                            else if (challenge.access.equals("public")) {
                                return null;
                            }
                            // If this is a private challenge, then we first check to see if they are in invited
                            else if (challenge.invitedMembers.contains(user)){
                                return null;
                            }
                            else {
                                // Then we check to see if they are a friend of the owner
                                User owner = User.readUser(challenge.owner, ItemType.getItemType(challenge.owner));
                                if (owner.friends.contains(user)) {
                                    return null;
                                }
                                else {
                                    return "User not allowed to join the private challenge without being friends with" +
                                            " the owner or explicitly invited!";
                                }
                            }
                        }
                        else {
                            return "That challenge is already filled up!";
                        }
                    }
                });
    }

    public static DatabaseAction updateRemoveMember(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "members", new AttributeValue(user), false, "DELETE");
    }

    public static DatabaseAction updateAddInvitedMember(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "invitedMembers", new AttributeValue(user), false, "ADD");
    }

    public static DatabaseAction updateRemoveInvitedMember(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "invitedMembers", new AttributeValue(user), false, "DELETE");
    }

    public static DatabaseAction updateAddMemberRequest(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "memberRequests", new AttributeValue(user), false, "ADD", new CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                Challenge challenge = (Challenge) newObject;
                if (challenge.restriction.equals("invite")) {
                    // Check to see if the member was already invited
                    if (!challenge.access.equals("public")) {
                        // If this is a private challenge then we check to see if they are a friend of the owner
                        User owner = User.readUser(challenge.owner, ItemType.getItemType(challenge.owner));
                        if (!owner.friends.contains(user)) {
                            return "User not allowed to ask to join the private challenge without being friends with" +
                                    " the owner!";
                        }
                    }

                    if ((challenge.members.contains(user))) {
                        return "That challenge already has that member request as a member!";
                    }
                    if (challenge.invitedMembers.contains(user)) {
                        return "This member was already invited to the Challenge!";
                    }
                    if ((challenge.memberRequests.contains(user))) {
                        return "That challenge already has that member request in their member requests!";
                    }
                    if (challenge.ifCompleted) {
                        return "That challenge has already been completed!";
                    }
                }
                else {
                    return "You can only add a member request for a challenge that is restricted to invite-only!";
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateRemoveMemberRequest(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "memberRequests", new AttributeValue(user), false, "DELETE");
    }

    public static DatabaseAction updateCapacity(String id, String capacity) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "capacity", new AttributeValue(capacity), false, "PUT");
    }

    public static DatabaseAction updateAddTag(String id, String tag) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "tags", new AttributeValue(tag), false, "ADD");
    }

    public static DatabaseAction updateRemoveTag(String id, String tag) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "tags", new AttributeValue(tag), false, "DELETE");
    }

    public static DatabaseAction updateWinner(String id, String winner) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "winner", new AttributeValue(winner), false, "PUT", new CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                // Winner must be a member of the challenge, and the challenge must have at least started
                Challenge challenge = (Challenge)newObject;
                if (challenge.members.contains(winner)) {
                    // TODO This might be dangerous, given that this could be running in any part of the country.....
                    // TODO TEST TEST TEST TEST TEST
                    if (TimeInterval.timeHasPassed(new DateTime(challenge.endTime))) {
                        return null;
                    }
                    else {
                        return "The challenge due date must have passed before declaring a winner!!!\n" +
                                "Challenge endTime = " + challenge.endTime.toString() + ". Now time = " +
                                (new DateTime()).toString() + ".";
                    }
                }
                else {
                    return "The winner must be a member of the actual challenge!";
                }
            }
        });
    }

    public static DatabaseAction updateAddEvent(String id, String event, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "events", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "events", new AttributeValue(event), false, "ADD");
        }
    }

    public static DatabaseAction updateRemoveEvent(String id, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "events", new AttributeValue(event), false, "DELETE");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}