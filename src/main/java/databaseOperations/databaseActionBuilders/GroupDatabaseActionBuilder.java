package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.Logic.ItemType;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.User;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGroupRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class GroupDatabaseActionBuilder {
    final static private String itemType = "Group";

    public static DatabaseAction create(CreateGroupRequest createGroupRequest) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Group.getEmptyItem();
        item.put("title", new AttributeValue(createGroupRequest.title));
        item.put("description", new AttributeValue(createGroupRequest.description));
        if (createGroupRequest.access != null) { item.put("access", new AttributeValue(createGroupRequest.access)); }
        if (createGroupRequest.restriction != null) { item.put("restriction", new AttributeValue(createGroupRequest
                .restriction)); }
        if (createGroupRequest.members != null) { item.put("members", new AttributeValue(Arrays.asList(createGroupRequest
                .members))); }
        if (createGroupRequest.owners != null) { item.put("owners", new AttributeValue(Arrays.asList(createGroupRequest
                .owners))); }
        if (createGroupRequest.tags != null) { item.put("tags", new AttributeValue(Arrays.asList(createGroupRequest
                .tags))); }
        return new CreateDatabaseAction(item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction updateTitle(String id, String title) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "title", new AttributeValue(title), false, PUT);
    }

    public static DatabaseAction updateDescription(String id, String description) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "description", new AttributeValue(description), false, PUT);
    }

    public static DatabaseAction updateAccess(String id, String access) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "access", new AttributeValue(access), false, PUT);
    }

    public static DatabaseAction updateRestriction(String id, String restriction) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "restriction", new AttributeValue(restriction), false, PUT);
    }

    public static DatabaseAction updateAddOwner(String id, String owner) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "owners", new AttributeValue(owner), false, ADD);
    }

    public static DatabaseAction updateRemoveOwner(String id, String owner) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "owners", new AttributeValue(owner), false, DELETE, new CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                Group group = (Group)newObject;
                if (group.owners.contains(owner) && group.owners.size() == 1) {
                    return "The Group cannot be left owner-less, you're the last owner!";
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateAddMember(String id, String user, boolean ifAccepting) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "members", new AttributeValue(user), false, ADD, new
                CheckHandler() {
                    @Override
                    public String isViable(DatabaseObject newObject) throws Exception {
                        // The capacity for the challenge must not be filled up yet.
                        Group group = (Group) newObject;
                        // Check to see if we are accepting a member request
                        if (ifAccepting) {
                            if (group.memberRequests.contains(user)) {
                                return null;
                            }
                            else {
                                return "You need to have a member request in order to accept it!";
                            }
                        }
                        else if (group.access.equals("public")) {
                            return null;
                        }
                        // If this is a private group, then we first check to see if they are in invited
                        else if (group.invitedMembers.contains(user)){
                            return null;
                        }
                        else {
                            // Then we check to see if they are a friend of the owner
                            for (String ownerID: group.owners) {
                                if ((User.readUser(ownerID, ItemType.getItemType(ownerID))).friends.contains(user)) {
                                    return null;
                                }
                            }
                            return "User not allowed to join the private group without being friends with" +
                                        " one of the owners or explicitly invited!";
                        }
                    }
                });
    }

    public static DatabaseAction updateRemoveMember(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "members", new AttributeValue(user), false, DELETE);
    }

    public static DatabaseAction updateAddInvitedMember(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "invitedMembers", new AttributeValue(user), false, ADD);
    }

    public static DatabaseAction updateRemoveInvitedMember(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "invitedMembers", new AttributeValue(user), false, DELETE);
    }

    public static DatabaseAction updateAddMemberRequest(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "memberRequests", new AttributeValue(user), false, ADD, new CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                Group group = (Group) newObject;
                if (group.restriction.equals("invite")) {
                    // Check to see if the member was already invited
                    if (!group.access.equals("public")) {
                        // If this is a private group then we check to see if they are a friend of the owner
                        boolean ifInFriends = false;
                        for (String ownerID : group.owners) {
                            User owner = User.readUser(ownerID, ItemType.getItemType(ownerID));
                            if (owner.friends.contains(user)) {
                                ifInFriends = true;
                                break;
                            }
                        }
                        if (!ifInFriends) {
                            return "User not allowed to ask to join the private group without being friends with" +
                                    " the owner!";
                        }
                    }

                    if ((group.members.contains(user))) {
                        return "That group already has that member request as a member!";
                    }
                    if (group.invitedMembers.contains(user)) {
                        return "This member was already invited to the Challenge!";
                    }
                    if ((group.memberRequests.contains(user))) {
                        return "That group already has that member request in their member requests!";
                    }
                }
                else {
                    return "You can only add a member request for a group that is restricted to invite-only!";
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateRemoveMemberRequest(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "memberRequests", new AttributeValue(user), false, DELETE);
    }

    public static DatabaseAction updateAddReceivedInvite(String id, String invite, boolean ifWithCreate) throws
            Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "receivedInvites", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "receivedInvites", new AttributeValue(invite), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveReceivedInvite(String id, String invite) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "receivedInvites", new AttributeValue(invite), false, DELETE);
    }

    public static DatabaseAction updateAddEvent(String id, String event, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "events", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "events", new AttributeValue(event), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveEvent(String id, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "events", new AttributeValue(event), false, DELETE);
    }

    public static DatabaseAction updateAddCompletedEvent(String id, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedEvents", new AttributeValue(event), false, ADD);
    }

    public static DatabaseAction updateRemoveCompletedEvent(String id, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedEvents", new AttributeValue(event), false, DELETE);
    }

    public static DatabaseAction updateAddChallenge(String id, String challenge, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "challenges", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "challenges", new AttributeValue(challenge), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "challenges", new AttributeValue(challenge), false, DELETE);
    }

    public static DatabaseAction updateAddCompletedChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedChallenges", new AttributeValue(challenge), false, ADD);
    }

    public static DatabaseAction updateRemoveCompletedChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedChallenges", new AttributeValue(challenge), false,
                DELETE);
    }

    public static DatabaseAction updateAddPost(String id, String post, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "posts", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "posts", new AttributeValue(post), false, ADD);
        }
    }

    public static DatabaseAction updateRemovePost(String id, String post) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "posts", new AttributeValue(post), false,
                DELETE);
    }

    public static DatabaseAction updateAddTag(String id, String tag) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "tags", new AttributeValue(tag), false, ADD);
    }

    public static DatabaseAction updateRemoveTag(String id, String tag) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "tags", new AttributeValue(tag), false, DELETE);
    }

    public static DatabaseAction updateGroup(String id, String group) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "group", new AttributeValue(group), false, PUT);
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
