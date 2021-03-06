package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.logic.ItemType;
import main.java.databaseObjects.DatabaseItem;
import main.java.databaseObjects.Group;
import main.java.databaseObjects.User;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGroupRequest;

import java.util.Arrays;
import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

/**
 * The Database Action Builder for the {@link Group} object, getting the {@link DatabaseAction} objects
 * that dictate the individual actions to do in the database for Groups.
 */
public class GroupDatabaseActionBuilder {
    final static private String itemType = "Group";

    /**
     * Gets the {@link PrimaryKey} object to identify the object in the database.
     *
     * @param id The ID of the object to reference.
     * @return The {@link PrimaryKey} object to identify the database item with.
     */
    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateGroupRequest createGroupRequest, Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Group.getEmptyItem();
        item.put("title", new AttributeValue(createGroupRequest.title));
        item.put("access", new AttributeValue(createGroupRequest.access));
        if (createGroupRequest.description != null) { item.put("description", new AttributeValue(
                createGroupRequest.description)); }
        if (createGroupRequest.motto != null) { item.put("motto", new AttributeValue(
                createGroupRequest .motto)); }
        if (createGroupRequest.groupImagePath != null) { item.put("groupImagePath", new
                AttributeValue(createGroupRequest .groupImagePath)); }
        if (createGroupRequest.restriction != null) { item.put("restriction", new AttributeValue(
                createGroupRequest .restriction)); }
        if (createGroupRequest.members != null) { item.put("members", new AttributeValue(
                Arrays.asList(createGroupRequest .members))); }
        if (createGroupRequest.owners != null) { item.put("owners", new AttributeValue(
                Arrays.asList(createGroupRequest .owners))); }
        if (createGroupRequest.tags != null) { item.put("tags", new AttributeValue(
                Arrays.asList(createGroupRequest .tags))); }
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
            (Map<String, AttributeValue> createdItem, String id) -> {
                if (createGroupRequest.groupImagePath != null) {
                    createdItem.put("groupImagePath", new AttributeValue(
                            id + "/" + createGroupRequest.groupImagePath
                    ));
                }
            }
        );
    }

    public static DatabaseAction updateTitle(String id, String title) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "title",
                new AttributeValue(title), false, PUT);
    }

    public static DatabaseAction updateDescription(String id, String description) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "description",
                new AttributeValue(description), false, PUT);
    }

    public static DatabaseAction updateMotto(String id, String motto) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "motto",
                new AttributeValue(motto), false, PUT);
    }

    public static DatabaseAction updateGroupImagePath(String id, String groupImagePath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "groupImagePath", new AttributeValue(groupImagePath), false, PUT);
    }

    public static DatabaseAction updateAccess(String id, String access) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "access", new AttributeValue(access), false, PUT);
    }

    public static DatabaseAction updateRestriction(String id, String restriction) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "restriction", new AttributeValue(restriction), false, PUT);
    }

    public static DatabaseAction updateAddOwner(String id, String owner) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "owners", new AttributeValue(owner), false, ADD);
    }

    public static DatabaseAction updateRemoveOwner(String id, String owner) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "owners", new AttributeValue(owner), false, DELETE, new CheckHandler() {
            @Override
            public String isViable(DatabaseItem newItem) throws Exception {
                Group group = (Group) newItem;
                if (group.owners.contains(owner) && group.owners.size() == 1) {
                    return "The Group cannot be left owner-less, you're the last owner!";
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateAddMember(String id, String user, boolean ifAccepting) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "members", new AttributeValue(user), false, ADD, new
                CheckHandler() {
                    @Override
                    public String isViable(DatabaseItem newItem) throws Exception {
                        // The capacity for the challenge must not be filled up yet.
                        Group group = (Group) newItem;
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
                Group group = (Group) newItem;
                if (group.restriction != null && group.restriction.equals("invite")) {
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

    public static DatabaseAction updateAddEvent(String id, String event, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "events", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "events", new AttributeValue(event), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveEvent(String id, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "events", new AttributeValue(event), false, DELETE);
    }

    public static DatabaseAction updateAddCompletedEvent(String id, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "completedEvents", new AttributeValue(event), false, ADD);
    }

    public static DatabaseAction updateRemoveCompletedEvent(String id, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "completedEvents", new AttributeValue(event), false, DELETE);
    }

    public static DatabaseAction updateAddChallenge(String id, String challenge, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "challenges", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "challenges", new AttributeValue(challenge), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "challenges", new AttributeValue(challenge), false, DELETE);
    }

    public static DatabaseAction updateAddCompletedChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "completedChallenges", new AttributeValue(challenge), false, ADD);
    }

    public static DatabaseAction updateRemoveCompletedChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "completedChallenges", new AttributeValue(challenge), false,
                DELETE);
    }

    public static DatabaseAction updateAddPost(String id, String post, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "posts", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "posts", new AttributeValue(post), false, ADD);
        }
    }

    public static DatabaseAction updateRemovePost(String id, String post) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "posts", new AttributeValue(post), false,
                DELETE);
    }

    public static DatabaseAction updateAddTag(String id, String tag) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "tags", new AttributeValue(tag), false, ADD);
    }

    public static DatabaseAction updateRemoveTag(String id, String tag) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "tags", new AttributeValue(tag), false, DELETE);
    }

    public static DatabaseAction updateAddStreak(String id, String streak, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "streaks", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "streaks", new AttributeValue(streak), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveStreak(String id, String streak) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "streaks", new AttributeValue(streak), false, DELETE);
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
