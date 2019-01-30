package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.*;
import main.java.databaseOperations.CheckHandler;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.UpdateDatabaseAction;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class UserDatabaseActionBuilder {
    private static PrimaryKey getPrimaryKey(String itemType, String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }
    
    public static DatabaseAction updateName(String id, String itemType, String name) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "name", new AttributeValue(name), false, PUT);
    }

    public static DatabaseAction updateGender(String id, String itemType, String gender) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "gender", new AttributeValue(gender), false, PUT);
    }

    public static DatabaseAction updateBirthday(String id, String itemType, String birthday) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "birthday", new AttributeValue(birthday), false, PUT);
    }

    public static DatabaseAction updateEmail(String id, String itemType, String email) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "email", new AttributeValue(email), false, PUT);
    }

    public static DatabaseAction updateStripeID(String id, String itemType, String stripeID) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "stripeID", new AttributeValue(stripeID), false, PUT);
    }

    public static DatabaseAction updateProfileImagePath(String id, String itemType, String profileImagePath) throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "profileImagePath", new AttributeValue(profileImagePath),
                false, PUT);
    }

    public static DatabaseAction updateAddProfileImagePath(String id, String itemType, String profileImagePath) throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "profileImagePaths", new AttributeValue(profileImagePath),
                false, ADD);
    }

    public static DatabaseAction updateRemoveProfileImagePath(String id, String itemType, String profileImagePath)
            throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "profileImagePaths", new AttributeValue(profileImagePath),
                false, DELETE);
    }

    public static DatabaseAction updateAddScheduledWorkout(String id, String itemType, String workout, boolean
            ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "scheduledWorkouts", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "scheduledWorkouts", new AttributeValue(workout),
                        true, ADD);
        }
    }

    public static DatabaseAction updateRemoveScheduledWorkout(String id, String itemType, String workout) throws
            Exception {
        // TODO REFUNDS?
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "scheduledWorkouts", new AttributeValue(workout), false,
                DELETE);
    }

    public static DatabaseAction updateAddCompletedWorkout(String id, String itemType, String workout) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "completedWorkouts", new AttributeValue(workout), false,
                ADD);
    }

    public static DatabaseAction updateRemoveCompletedWorkout(String id, String itemType, String workout) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "completedWorkouts", new AttributeValue(workout), false,
                DELETE);
    }

    public static DatabaseAction updateAddScheduledTime(String id, String itemType, String time, CheckHandler checkHandler)
            throws Exception {
        if (checkHandler == null) {
            TimeInterval timeInterval = new TimeInterval(time);
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "scheduledTimes", new AttributeValue(time),
                    false, ADD, new CheckHandler() {
                @Override
                public String isViable(DatabaseItem newItem) throws Exception {
                    // Check for conflicts in the schedule
                    User user = (User) newItem;
                    for (TimeInterval clientTimeInterval : user.scheduledTimes) {
                        if (timeInterval.intersects(clientTimeInterval)) {
                            // If it is intersecting with another time interval, then we can't place it!
                            return "That is intersecting with the client's existing schedule!";
                        }
                    }
                    return null;
                }
            });
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "scheduledTimes", new AttributeValue(time), false, ADD,
                    checkHandler);
        }
    }

    public static DatabaseAction updateRemoveScheduledTime(String id, String itemType, String time)
            throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "scheduledTimes", new AttributeValue(time),
                false, DELETE);
    }

    public static DatabaseAction updateAddReviewBy(String id, String itemType, String review, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "reviewsBy", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "reviewsBy", new AttributeValue(review),
                        true, ADD);
        }
    }

    public static DatabaseAction updateRemoveReviewBy(String id, String itemType, String review) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "reviewsBy", new AttributeValue(review), false, DELETE);
    }

    public static DatabaseAction updateAddReviewAbout(String id, String itemType, String review, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "reviewsAbout", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "reviewsAbout", new AttributeValue(review),
                        true, ADD);
        }
    }

    public static DatabaseAction updateRemoveReviewAbout(String id, String itemType, String review) throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "reviewsAbout", new AttributeValue(review), false, DELETE);
    }

    public static DatabaseAction updateFriendlinessRating(String id, String itemType, String rating) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "friendlinessRating", new AttributeValue(rating), false, PUT);
    }

    public static DatabaseAction updateEffectivenessRating(String id, String itemType, String rating) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "effectivenessRating", new AttributeValue(rating), false, PUT);
    }

    public static DatabaseAction updateReliabilityRating(String id, String itemType, String rating) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "reliabilityRating", new AttributeValue(rating), false, PUT);
    }

    public static DatabaseAction updateBio(String id, String itemType, String bio) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "bio", new AttributeValue(bio), false, PUT);
    }

    public static DatabaseAction updateAddFriend(String id, String itemType, String friend, boolean ifAccepting) throws
            Exception {
        // If the person is accepting the request, make sure they actually have the request
        if (ifAccepting) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "friends", new AttributeValue(friend),
                    false, ADD, new CheckHandler() {
                @Override
                public String isViable(DatabaseItem newItem) throws Exception {
                    if (!((User) newItem).friendRequests.contains(friend)) {
                        return "Add friend was not in the user's friend requests!";
                    }
                    return null;
                }
            });
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "friends", new AttributeValue(friend),
                    false, ADD);
        }
    }

    public static DatabaseAction updateRemoveFriend(String id, String itemType, String friend) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "friends", new AttributeValue(friend),
                false, DELETE);
    }

    public static DatabaseAction updateAddFriendRequest(String id, String itemType, String friend) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "friendRequests", new AttributeValue(friend),
                false, ADD, new CheckHandler() {
            @Override
            public String isViable(DatabaseItem newItem) throws Exception {
                // TODO Check if the friend request isn't in the requests already either
                User user = (User) newItem;
                if ((user.friends.contains(friend))) {
                    return "That person already has that friend request as a friend!";
                }
                if ((user.friendRequests.contains(friend))) {
                    return "That person already has that friend request in their friend requests!";
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateRemoveFriendRequest(String id, String itemType, String friend) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "friendRequests", new AttributeValue(friend),
                false, DELETE);
    }

    public static DatabaseAction updateAddChallenge(String id, String itemType, String challenge, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "challenges", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "challenges", new AttributeValue(challenge),
                    false, ADD);
        }
    }

    public static DatabaseAction updateRemoveChallenge(String id, String itemType, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "challenges", new AttributeValue(challenge), false,
                DELETE);
    }

    public static DatabaseAction updateAddCompletedChallenge(String id, String itemType, String challenge) throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "completedChallenges", new AttributeValue(challenge), false,
                ADD);
    }

    public static DatabaseAction updateRemoveCompletedChallenge(String id, String itemType, String challenge) throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "completedChallenges", new AttributeValue(challenge), false,
                DELETE);
    }

    public static DatabaseAction updateAddOwnedChallenge(String id, String itemType, String challenge, boolean
            ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "ownedChallenges", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "ownedChallenges", new AttributeValue(challenge),
                    true, ADD);
        }
    }

    public static DatabaseAction updateRemoveOwnedChallenge(String id, String itemType, String challenge) throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "ownedChallenges", new AttributeValue(challenge), false,
                DELETE);
    }

    public static DatabaseAction updateAddInvitedChallenge(String id, String itemType, String challenge) throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "invitedChallenges", new AttributeValue(challenge), false,
                ADD, new CheckHandler() {
            @Override
            public String isViable(DatabaseItem newItem) throws Exception {
                if (((User) newItem).invitedChallenges.contains(challenge)) {
                    return "That user was already invited to that challenge!";
                }
                if (((User) newItem).challenges.contains(challenge)) {
                    return "That user is already a part of that challenge!";
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateRemoveInvitedChallenge(String id, String itemType, String challenge) throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "invitedChallenges", new AttributeValue(challenge), false,
                DELETE);
    }

    public static DatabaseAction updateAddChallengeWon(String id, String itemType, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "challengesWon", new AttributeValue(challenge), false, ADD);
    }

    public static DatabaseAction updateRemoveChallengeWon(String id, String itemType, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "challengesWon", new AttributeValue(challenge), false, DELETE);
    }

    public static DatabaseAction updateAddScheduledEvent(String id, String itemType, String event, boolean ifWithCreate) throws
            Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "scheduledEvents", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "scheduledEvents", new AttributeValue(event), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveScheduledEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "scheduledEvents", new AttributeValue(event), false,
                DELETE);
    }

    public static DatabaseAction updateAddCompletedEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "completedEvents", new AttributeValue(event), false,
                ADD);
    }

    public static DatabaseAction updateRemoveCompletedEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "completedEvents", new AttributeValue(event), false,
                DELETE);
    }

    public static DatabaseAction updateAddOwnedEvent(String id, String itemType, String event, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "ownedEvents", null, true,
                    ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "ownedEvents", new AttributeValue(event), false,
                    ADD);
        }
    }

    public static DatabaseAction updateRemoveOwnedEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "ownedEvents", new AttributeValue(event), false,
                DELETE);
    }

    public static DatabaseAction updateAddInvitedEvent(String id, String itemType, String event) throws
            Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "invitedEvents", new AttributeValue(event), false, ADD, new
                CheckHandler() {
                    @Override
                    public String isViable(DatabaseItem newItem) throws Exception {
                        if (((User) newItem).invitedEvents.contains(event)) {
                            return "That user was already invited to that event!";
                        }
                        if (((User) newItem).scheduledEvents.contains(event)) {
                            return "That user is already a part of that event!";
                        }
                        return null;
                    }
                });
    }

    public static DatabaseAction updateRemoveInvitedEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "invitedEvents", new AttributeValue(event), false, DELETE);
    }

    public static DatabaseAction updateAddSentInvite(String id, String itemType, String invite, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "sentInvites", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "sentInvites", new AttributeValue(invite), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveSentInvite(String id, String itemType, String invite) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "sentInvites", new AttributeValue(invite), false, DELETE);
    }

    public static DatabaseAction updateAddReceivedInvite(String id, String itemType, String invite, boolean
            ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "receivedInvites", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "receivedInvites", new AttributeValue(invite), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveReceivedInvite(String id, String itemType, String invite) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "receivedInvites", new AttributeValue(invite),
                false, DELETE);
    }

    public static DatabaseAction updateAddPost(String id, String itemType, String post, boolean ifWithCreate) throws
            Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "posts", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "posts", new AttributeValue(post), false, ADD);
        }
    }

    public static DatabaseAction updateRemovePost(String id, String itemType, String post) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "posts", new AttributeValue(post), false, DELETE);
    }

    public static DatabaseAction updateAddLike(String id, String itemType, String like) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "likes", new AttributeValue(like), false, ADD);
    }

    public static DatabaseAction updateRemoveLike(String id, String itemType, String like) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "likes", new AttributeValue(like), false, DELETE);
    }

    public static DatabaseAction updateAddComment(String id, String itemType, String comment, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "comments", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "comments", new AttributeValue(comment), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveComment(String id, String itemType, String comment) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "comments", new AttributeValue(comment), false, DELETE);
    }

    public static DatabaseAction updateAddGroup(String id, String itemType, String group, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "groups", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "groups", new AttributeValue(group), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveGroup(String id, String itemType, String group) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "groups", new AttributeValue(group), false, DELETE);
    }

    public static DatabaseAction updateAddInvitedGroup(String id, String itemType, String group) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "invitedGroups", new AttributeValue(group), false, ADD, new CheckHandler() {
            @Override
            public String isViable(DatabaseItem newItem) throws Exception {
                if (((User) newItem).invitedGroups.contains(group)) {
                    return "That user was already invited to that group!";
                }
                if (((User) newItem).groups.contains(group)) {
                    return "That user is already a part of that group!";
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateRemoveInvitedGroup(String id, String itemType, String group) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "invitedGroups", new AttributeValue(group), false, DELETE);
    }

    public static DatabaseAction updateAddOwnedGroup(String id, String itemType, String group, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "ownedGroups", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "ownedGroups", new AttributeValue(group), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveOwnedGroup(String id, String itemType, String group) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "ownedGroups", new AttributeValue(group), false, DELETE);
    }

    public static DatabaseAction updateAddMessageBoard(String id, String itemType, String messageBoard) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "messageBoards", new AttributeValue(messageBoard), false, ADD);
    }

    public static DatabaseAction updateRemoveMessageBoard(String id, String itemType, String messageBoard) throws Exception {
        return new UpdateDatabaseAction(id, getPrimaryKey(itemType, id), "messageBoards", new AttributeValue(messageBoard), false, DELETE);
    }
}
