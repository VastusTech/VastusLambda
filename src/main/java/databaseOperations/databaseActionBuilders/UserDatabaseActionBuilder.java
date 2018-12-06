package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.Invite;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseObjects.User;
import main.java.databaseOperations.CheckHandler;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.UpdateDatabaseAction;

public class UserDatabaseActionBuilder {
    public static DatabaseAction updateName(String id, String itemType, String name) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "name", new AttributeValue(name), false, "PUT");
    }

    public static DatabaseAction updateGender(String id, String itemType, String gender) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "gender", new AttributeValue(gender), false, "PUT");
    }

    public static DatabaseAction updateBirthday(String id, String itemType, String birthday) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "birthday", new AttributeValue(birthday), false, "PUT");
    }

    public static DatabaseAction updateEmail(String id, String itemType, String email) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "email", new AttributeValue(email), false, "PUT");
    }

    public static DatabaseAction updateProfileImagePath(String id, String itemType, String profileImagePath) throws
            Exception {
        return new UpdateDatabaseAction(id, itemType, "profileImagePath", new AttributeValue(profileImagePath),
                false, "PUT");
    }

    public static DatabaseAction updateAddProfileImagePath(String id, String itemType, String profileImagePath) throws
            Exception {
        return new UpdateDatabaseAction(id, itemType, "profileImagePaths", new AttributeValue(profileImagePath),
                false, "ADD");
    }

    public static DatabaseAction updateRemoveProfileImagePath(String id, String itemType, String profileImagePath)
            throws
            Exception {
        return new UpdateDatabaseAction(id, itemType, "profileImagePaths", new AttributeValue(profileImagePath),
                false, "DELETE");
    }

    public static DatabaseAction updateAddScheduledWorkout(String id, String itemType, String workout, boolean
            ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "scheduledWorkouts", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "scheduledWorkouts", new AttributeValue(workout),
                        true, "ADD");
        }
    }

    public static DatabaseAction updateRemoveScheduledWorkout(String id, String itemType, String workout) throws
            Exception {
        // TODO REFUNDS?
        return new UpdateDatabaseAction(id, itemType, "scheduledWorkouts", new AttributeValue(workout), false,
                "DELETE");
    }

    public static DatabaseAction updateAddCompletedWorkout(String id, String itemType, String workout) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedWorkouts", new AttributeValue(workout), false,
                "ADD");
    }

    public static DatabaseAction updateRemoveCompletedWorkout(String id, String itemType, String workout) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedWorkouts", new AttributeValue(workout), false,
                "DELETE");
    }

    public static DatabaseAction updateAddScheduledTime(String id, String itemType, String time, CheckHandler checkHandler)
            throws Exception {
        if (checkHandler == null) {
            TimeInterval timeInterval = new TimeInterval(time);
            return new UpdateDatabaseAction(id, itemType, "scheduledTimes", new AttributeValue(time),
                    false, "ADD", new CheckHandler() {
                @Override
                public String isViable(DatabaseObject newObject) throws Exception {
                    // Check for conflicts in the schedule
                    User user = (User) newObject;
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
            return new UpdateDatabaseAction(id, itemType, "scheduledTimes", new AttributeValue(time), false, "ADD",
                    checkHandler);
        }
    }

    public static DatabaseAction updateRemoveScheduledTime(String id, String itemType, String time)
            throws Exception {
        return new UpdateDatabaseAction(id, itemType, "scheduledTimes", new AttributeValue(time),
                false, "DELETE");
    }

    public static DatabaseAction updateAddReviewBy(String id, String itemType, String review, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "reviewsBy", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "reviewsBy", new AttributeValue(review),
                        true, "ADD");
        }
    }

    public static DatabaseAction updateRemoveReviewBy(String id, String itemType, String review) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "reviewsBy", new AttributeValue(review), false, "DELETE");
    }

    public static DatabaseAction updateAddReviewAbout(String id, String itemType, String review, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "reviewsAbout", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "reviewsAbout", new AttributeValue(review),
                        true, "ADD");
        }
    }

    public static DatabaseAction updateRemoveReviewAbout(String id, String itemType, String review) throws
            Exception {
        return new UpdateDatabaseAction(id, itemType, "reviewsAbout", new AttributeValue(review), false, "DELETE");
    }

    public static DatabaseAction updateFriendlinessRating(String id, String itemType, String rating) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friendlinessRating", new AttributeValue(rating), false, "PUT");
    }

    public static DatabaseAction updateEffectivenessRating(String id, String itemType, String rating) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "effectivenessRating", new AttributeValue(rating), false, "PUT");
    }

    public static DatabaseAction updateReliabilityRating(String id, String itemType, String rating) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "reliabilityRating", new AttributeValue(rating), false, "PUT");
    }

    public static DatabaseAction updateBio(String id, String itemType, String bio) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "bio", new AttributeValue(bio), false, "PUT");
    }

    public static DatabaseAction updateAddFriend(String id, String itemType, String friend, boolean ifAccepting) throws
            Exception {
        // If the person is accepting the request, make sure they actually have the request
        if (ifAccepting) {
            return new UpdateDatabaseAction(id, itemType, "friends", new AttributeValue(friend),
                    false, "ADD", new CheckHandler() {
                @Override
                public String isViable(DatabaseObject newObject) throws Exception {
                    if (!((User)newObject).friendRequests.contains(friend)) {
                        return "Add friend was not in the user's friend requests!";
                    }
                    return null;
                }
            });
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "friends", new AttributeValue(friend),
                    false, "ADD");
        }
    }

    public static DatabaseAction updateRemoveFriend(String id, String itemType, String friend) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friends", new AttributeValue(friend),
                false, "DELETE");
    }

    public static DatabaseAction updateAddFriendRequest(String id, String itemType, String friend) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friendRequests", new AttributeValue(friend),
                false, "ADD", new CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                // TODO Check if the friend request isn't in the requests already either
                User user = (User) newObject;
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
        return new UpdateDatabaseAction(id, itemType, "friendRequests", new AttributeValue(friend),
                false, "DELETE");
    }

    public static DatabaseAction updateAddChallengeWon(String id, String itemType, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "challengesWon", new AttributeValue(challenge), false, "ADD");
    }

    public static DatabaseAction updateRemoveChallengeWon(String id, String itemType, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "challengesWon", new AttributeValue(challenge), false, "DELETE");
    }

    public static DatabaseAction updateAddScheduledEvent(String id, String itemType, String event, boolean ifWithCreate) throws
            Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "scheduledEvents", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "scheduledEvents", new AttributeValue(event), false, "ADD");
        }
    }

    public static DatabaseAction updateRemoveScheduledEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "scheduledEvents", new AttributeValue(event), false,
                "DELETE");
    }

    public static DatabaseAction updateAddCompletedEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedEvents", new AttributeValue(event), false,
                "ADD");
    }

    public static DatabaseAction updateRemoveCompletedEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedEvents", new AttributeValue(event), false,
                "DELETE");
    }

    public static DatabaseAction updateAddOwnedEvent(String id, String itemType, String event, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "ownedEvents", null, true,
                    "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "ownedEvents", new AttributeValue(event), false,
                    "ADD");
        }
    }

    public static DatabaseAction updateRemoveOwnedEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "ownedEvents", new AttributeValue(event), false,
                "DELETE");
    }

    public static DatabaseAction updateAddInvitedEvent(String id, String itemType, String event) throws
            Exception {
        return new UpdateDatabaseAction(id, itemType, "invitedEvents", new AttributeValue(event), false, "ADD", new
                CheckHandler() {
                    @Override
                    public String isViable(DatabaseObject newObject) throws Exception {
                        if (((User)newObject).invitedEvents.contains(event)) {
                            return "That user was already invited to that event!";
                        }
                        if (((User)newObject).scheduledEvents.contains(event)) {
                            return "That user is already a part of that event!";
                        }
                        return null;
                    }
                });
    }

    public static DatabaseAction updateRemoveInvitedEvent(String id, String itemType, String event) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "invitedEvents", new AttributeValue(event), false, "DELETE");
    }

    public static DatabaseAction updateAddSentInvite(String id, String itemType, String invite, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "sentInvites", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "sentInvites", new AttributeValue(invite), false, "ADD");
        }
    }

    public static DatabaseAction updateRemoveSentInvite(String id, String itemType, String invite) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "sentInvites", new AttributeValue(invite), false, "DELETE");
    }

    public static DatabaseAction updateAddReceivedInvite(String id, String itemType, String invite, boolean
            ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "receivedInvites", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "receivedInvites", new AttributeValue(invite), false, "ADD");
        }
    }

    public static DatabaseAction updateRemoveReceivedInvite(String id, String itemType, String invite) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "receivedInvites", new AttributeValue(invite),
                false, "DELETE");
    }
}
