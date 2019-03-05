package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.Logic.ItemType;
import main.java.databaseObjects.DatabaseItem;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.Event;
import main.java.databaseObjects.User;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateEventRequest;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class EventDatabaseActionBuilder {
    private static final String itemType = "Event";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateEventRequest createEventRequest, boolean ifWithCreate) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Event.getEmptyItem();
        item.put("owner", new AttributeValue(createEventRequest.owner));
        item.put("time", new AttributeValue(createEventRequest.time));
        item.put("capacity", new AttributeValue(createEventRequest.capacity));
        item.put("address", new AttributeValue(createEventRequest.address));
        item.put("title", new AttributeValue(createEventRequest.title));
        if (createEventRequest.challenge != null) { item.put("challenge", new AttributeValue(createEventRequest
                .challenge)); }
        if (createEventRequest.description != null) { item.put("description", new AttributeValue(createEventRequest
                .description)); }
        if (createEventRequest.members != null) { item.put("members", new AttributeValue
                (Arrays.asList(createEventRequest.members))); }
        if (createEventRequest.access != null) { item.put("access", new AttributeValue(createEventRequest
                .access)); }
        if (createEventRequest.restriction != null) { item.put("restriction", new AttributeValue(createEventRequest
                .restriction)); }
        if (createEventRequest.tags != null) { item.put("tags", new AttributeValue
                (Arrays.asList(createEventRequest.tags))); }
        return new CreateDatabaseAction(itemType, item, ifWithCreate, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction updateTitle(String id, String title) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "title", new AttributeValue(title), false, PUT);
    }

    public static DatabaseAction updateDescription(String id, String description) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "description", new AttributeValue(description), false, PUT);
    }

    public static DatabaseAction updateAddress(String id, String address) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "address", new AttributeValue(address), false, PUT);
    }

    public static DatabaseAction updateIfCompleted(String id, String ifCompleted) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "ifCompleted", new AttributeValue(ifCompleted), false, PUT);
    }

    public static DatabaseAction updateAccess(String id, String access) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "access", new AttributeValue(access), false, PUT);
    }

    public static DatabaseAction updateRestriction(String id, String restriction) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "restriction", new AttributeValue(restriction), false, PUT);
    }

    public static DatabaseAction updateChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "challenge", new AttributeValue(challenge), false, PUT);
    }

    public static DatabaseAction updateAddMember(String id, String user, boolean ifAccepting) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "members", new AttributeValue(user), false, ADD, new
                CheckHandler() {
                    @Override
                    public String isViable(DatabaseItem newItem) throws Exception {
                        // The capacity for the event must not be filled up yet.
                        Event event = (Event) newItem;
                        if (event.ifCompleted) {
                            return "That event has already been completed!";
                        }
                        if (event.capacity > event.members.size()) {
                            // Check to see if we are accepting a member request
                            if (ifAccepting) {
                                if (event.memberRequests.contains(user)) {
                                    return null;
                                }
                                else {
                                    return "You need to have a member request in order to accept it!";
                                }
                            }
                            else if (event.access.equals("public")) {
                                return null;
                            }
                            // If this is a private event, then we first check to see if they are in invited
                            else if (event.invitedMembers.contains(user)){
                                return null;
                            }
                            else {
                                // Then we check to see if they are a friend of the owner
                                User owner = User.readUser(event.owner, ItemType.getItemType(event.owner));
                                if (owner.friends.contains(user)) {
                                    return null;
                                }
                                else {
                                    return "User not allowed to join the private event without being friends with" +
                                            " the owner or explicitly invited!";
                                }
                            }
                        }
                        else {
                            return "That event is already filled up!";
                        }
                    }
                });
    }

    public static DatabaseAction updateRemoveMember(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "members", new AttributeValue(user), false, DELETE);
    }

    public static DatabaseAction updateAddInvitedMember(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "invitedMembers", new AttributeValue(user), false, ADD);
    }

    public static DatabaseAction updateRemoveInvitedMember(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "invitedMembers", new AttributeValue(user), false, DELETE);
    }

    public static DatabaseAction updateAddMemberRequest(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "memberRequests", new AttributeValue(user), false, ADD, new CheckHandler() {
            @Override
            public String isViable(DatabaseItem newItem) throws Exception {
                Event event = (Event) newItem;
                if (event.restriction != null && event.restriction.equals("invite")) {
                    // Check to see if the member was already invited
                    if (!event.access.equals("public")) {
                        // If this is a private event then we check to see if they are a friend of the owner
                        User owner = User.readUser(event.owner, ItemType.getItemType(event.owner));
                        if (!owner.friends.contains(user)) {
                            return "User not allowed to ask to join the private event without being friends with" +
                                    " the owner!";
                        }
                    }

                    if ((event.members.contains(user))) {
                        return "That event already has that member request as a member!";
                    }
                    if (event.invitedMembers.contains(user)) {
                        return "This member was already invited to the Challenge!";
                    }
                    if (event.memberRequests.contains(user)) {
                        return "That event already has that member request in their member requests!";
                    }
                    if (event.ifCompleted) {
                        return "That event has already been completed!";
                    }
                }
                else {
                    return "You can only add a member request for a event that is restricted to invite-only!";
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateRemoveMemberRequest(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "memberRequests", new AttributeValue(user), false, DELETE);
    }

    public static DatabaseAction updateAddReceivedInvite(String id, String invite, boolean ifWithCreate) throws
            Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "receivedInvites", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "receivedInvites", new AttributeValue(invite), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveReceivedInvite(String id, String invite) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "receivedInvites", new AttributeValue(invite), false, DELETE);
    }

    public static DatabaseAction updateCapacity(String id, String capacity) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "capacity", new AttributeValue(capacity), false, PUT);
    }

    public static DatabaseAction updateAddTag(String id, String tag) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "tags", new AttributeValue(tag), false, ADD);
    }

    public static DatabaseAction updateRemoveTag(String id, String tag) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "tags", new AttributeValue(tag), false, DELETE);
    }

    public static DatabaseAction updateGroup(String id, String group) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "group", new AttributeValue(group), false, PUT);
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}

