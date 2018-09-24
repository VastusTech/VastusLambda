package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Client;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;

import java.util.HashMap;
import java.util.Map;

public class ClientDatabaseActionBuilder {
    final static private String itemType = "Client";

    public static DatabaseAction create(CreateClientRequest createClientRequest) {
        // Handle the setting of the items!
        // TODO Make sure that the null values aren't too too problematic
        Map<String, AttributeValue> item = Client.getEmptyItem();
        item.put("name", new AttributeValue(createClientRequest.name));
        item.put("gender", new AttributeValue(createClientRequest.gender));
        item.put("birthday", new AttributeValue(createClientRequest.birthday));
        item.put("email", new AttributeValue(createClientRequest.email));
        item.put("username", new AttributeValue(createClientRequest.username));
        return new CreateDatabaseAction(item);
    }

    public static DatabaseAction updateName(String id, String name) throws Exception {
        return UserDatabaseActionBuilder.updateName(id, itemType, name);
    }

    public static DatabaseAction updateGender(String id, String gender) throws Exception {
        return UserDatabaseActionBuilder.updateGender(id, itemType, gender);
    }

    public static DatabaseAction updateBirthday(String id, String birthday) throws Exception {
        return UserDatabaseActionBuilder.updateBirthday(id, itemType, birthday);
    }

    public static DatabaseAction updateEmail(String id, String email) throws Exception {
        return UserDatabaseActionBuilder.updateEmail(id, itemType, email);
    }

    public static DatabaseAction updateProfileImagePath(String id, String profileImagePath) throws Exception {
        return UserDatabaseActionBuilder.updateProfileImagePath(id, itemType, profileImagePath);
    }

    public static DatabaseAction updateAddScheduledWorkout(String id, String workoutID, boolean ifWithCreate) throws
            Exception {
        return UserDatabaseActionBuilder.updateAddScheduledWorkout(id, itemType, workoutID, ifWithCreate);
    }

    public static DatabaseAction updateRemoveScheduledWorkout(String id, String workoutID) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveScheduledWorkout(id, itemType, workoutID);
    }

    public static DatabaseAction updateAddCompletedWorkout(String id, String workoutID) throws Exception {
        return UserDatabaseActionBuilder.updateAddCompletedWorkout(id, itemType, workoutID);
    }

    public static DatabaseAction updateRemoveCompletedWorkout(String id, String workoutID) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveCompletedWorkout(id, itemType, workoutID);
    }

    public static DatabaseAction updateAddScheduledWorkoutTime(String id, String workoutTime) throws Exception {
        TimeInterval workoutTimeInterval = new TimeInterval(workoutTime);
        return UserDatabaseActionBuilder.updateAddScheduledWorkoutTime(id, itemType, workoutTime, new CheckHandler() {
            @Override
            public boolean isViable(DatabaseObject newObject) throws Exception {
                // This is to check whether any times conflict
                Client client = (Client)newObject;
                for (TimeInterval clientWorkoutTimeInterval : client.scheduledWorkoutTimes) {
                    if (workoutTimeInterval.intersects(clientWorkoutTimeInterval)) {
                        // If it is intersecting with another time interval, then we can't place it!
                        return false;
                    }
                }
                return true;
            }
        });
    }

    public static DatabaseAction updateRemoveScheduledWorkoutTime(String id, String workoutTime) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveScheduledWorkoutTime(id, itemType, workoutTime);
    }

    public static DatabaseAction updateAddCompletedWorkoutTime(String id, String workoutTime) throws Exception {
        return UserDatabaseActionBuilder.updateAddCompletedWorkoutTime(id, itemType, workoutTime);
    }

    public static DatabaseAction updateRemoveCompletedWorkoutTime(String id, String workoutTime) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveCompletedWorkoutTime(id, itemType, workoutTime);
    }

    public static DatabaseAction updateAddReviewBy(String id, String reviewID, boolean ifWithCreate) throws Exception {
        return UserDatabaseActionBuilder.updateAddReviewBy(id, itemType, reviewID, ifWithCreate);
    }

    public static DatabaseAction updateRemoveReviewBy(String id, String reviewID) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveReviewBy(id, itemType, reviewID);
    }

    public static DatabaseAction updateAddReviewAbout(String id, String reviewID, boolean ifWithCreate) throws
            Exception {
        return UserDatabaseActionBuilder.updateAddReviewAbout(id, itemType, reviewID, ifWithCreate);
    }

    public static DatabaseAction updateRemoveReviewAbout(String id, String reviewID) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveReviewAbout(id, itemType, reviewID);
    }

    public static DatabaseAction updateFriendlinessRating(String id, String rating) throws Exception {
        return UserDatabaseActionBuilder.updateFriendlinessRating(id, itemType, rating);
    }

    public static DatabaseAction updateEffectivenessRating(String id, String rating) throws Exception {
        return UserDatabaseActionBuilder.updateEffectivenessRating(id, itemType, rating);
    }

    public static DatabaseAction updateReliabilityRating(String id, String rating) throws Exception {
        return UserDatabaseActionBuilder.updateReliabilityRating(id, itemType, rating);
    }

    public static DatabaseAction updateBio(String id, String bio) throws Exception {
        return UserDatabaseActionBuilder.updateBio(id, itemType, bio);
    }

    public static DatabaseAction updateAddFriend(String id, String friendID, boolean ifAccepting) throws Exception {
        // If the person is accepting the request, make sure they actually have the request
        if (ifAccepting) {
            return new UpdateDatabaseAction(id, itemType, "friends", new AttributeValue(friendID), false, "ADD", new CheckHandler() {
                @Override
                public boolean isViable(DatabaseObject newObject) throws Exception {
                    return (((Client)newObject).friendRequests.contains(friendID));
                }
            });
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "friends", new AttributeValue(friendID), false, "ADD");
        }
    }

    public static DatabaseAction updateRemoveFriend(String id, String friendID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friends", new AttributeValue(friendID), false, "REMOVE");
    }

    public static DatabaseAction updateAddFriendRequest(String id, String friendID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friend_requests", new AttributeValue(friendID), false, "ADD");
    }

    public static DatabaseAction updateRemoveFriendRequest(String id, String friendID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friend_requests", new AttributeValue(friendID), false, "REMOVE");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Client"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
