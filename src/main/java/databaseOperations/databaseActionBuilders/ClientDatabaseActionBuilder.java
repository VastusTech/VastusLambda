package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Client;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateClientRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClientDatabaseActionBuilder {
    final static private String itemType = "Client";

    public static DatabaseAction create(CreateClientRequest createClientRequest) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Client.getEmptyItem();
        item.put("name", new AttributeValue(createClientRequest.name));
        item.put("gender", new AttributeValue(createClientRequest.gender));
        item.put("birthday", new AttributeValue(createClientRequest.birthday));
        item.put("email", new AttributeValue(createClientRequest.email));
        item.put("username", new AttributeValue(createClientRequest.username));
        if (createClientRequest.bio != null) { item.put("bio", new AttributeValue(createClientRequest.bio)); }
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

    public static DatabaseAction updateAddScheduledWorkout(String id, String workout, boolean ifWithCreate) throws
            Exception {
        return UserDatabaseActionBuilder.updateAddScheduledWorkout(id, itemType, workout, ifWithCreate);
    }

    public static DatabaseAction updateRemoveScheduledWorkout(String id, String workout) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveScheduledWorkout(id, itemType, workout);
    }

    public static DatabaseAction updateAddCompletedWorkout(String id, String workout) throws Exception {
        return UserDatabaseActionBuilder.updateAddCompletedWorkout(id, itemType, workout);
    }

    public static DatabaseAction updateRemoveCompletedWorkout(String id, String workout) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveCompletedWorkout(id, itemType, workout);
    }

    public static DatabaseAction updateAddScheduledTime(String id, String time) throws Exception {
        TimeInterval timeInterval = new TimeInterval(time);
        return UserDatabaseActionBuilder.updateAddScheduledTime(id, itemType, time, new CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                // This is to check whether any times conflict
                Client client = (Client)newObject;
                for (TimeInterval clientTimeInterval : client.scheduledTimes) {
                    if (timeInterval.intersects(clientTimeInterval)) {
                        // If it is intersecting with another time interval, then we can't place it!
                        return "That is intersecting with the client's existing schedule!";
                    }
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateRemoveScheduledTime(String id, String time) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveScheduledTime(id, itemType, time);
    }

    public static DatabaseAction updateAddReviewBy(String id, String review, boolean ifWithCreate) throws Exception {
        return UserDatabaseActionBuilder.updateAddReviewBy(id, itemType, review, ifWithCreate);
    }

    public static DatabaseAction updateRemoveReviewBy(String id, String review) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveReviewBy(id, itemType, review);
    }

    public static DatabaseAction updateAddReviewAbout(String id, String review, boolean ifWithCreate) throws
            Exception {
        return UserDatabaseActionBuilder.updateAddReviewAbout(id, itemType, review, ifWithCreate);
    }

    public static DatabaseAction updateRemoveReviewAbout(String id, String review) throws Exception {
        return UserDatabaseActionBuilder.updateRemoveReviewAbout(id, itemType, review);
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

    public static DatabaseAction updateAddFriends(String id, String[] friends, boolean ifAccepting) throws Exception {
        // If the person is accepting the request, make sure they actually have the request
        if (ifAccepting) {
            return new UpdateDatabaseAction(id, itemType, "friends", new AttributeValue(Arrays.asList(friends)),
                    false, "ADD", new CheckHandler() {
                @Override
                public String isViable(DatabaseObject newObject) throws Exception {
                    for (String friend : friends) {
                        if (!((Client) newObject).friendRequests.contains(friend)) {
                            return "Add friend was not in the client's friend requests!";
                        }
                    }
                    return null;
                }
            });
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "friends", new AttributeValue(Arrays.asList(friends)),
                    false, "ADD");
        }
    }

    public static DatabaseAction updateRemoveFriends(String id, String[] friends) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friends", new AttributeValue(Arrays.asList(friends)),
                false, "DELETE");
    }

    public static DatabaseAction updateAddFriendRequests(String id, String[] friends) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friendRequests", new AttributeValue(Arrays.asList(friends)),
                false, "ADD");
    }

    public static DatabaseAction updateRemoveFriendRequests(String id, String[] friends) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friendRequests", new AttributeValue(Arrays.asList(friends)),
                false, "DELETE");
    }

    public static DatabaseAction updateAddChallengeWon(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "challengesWon", new AttributeValue(challenge), false, "ADD");
    }

    public static DatabaseAction updateRemoveChallengeWon(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "challengesWon", new AttributeValue(challenge), false, "DELETE");
    }

    public static DatabaseAction updateAddScheduledParty(String id, String party, boolean ifWithCreate) throws
            Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "scheduledParties", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "scheduledParties", new AttributeValue(party), false,
                    "ADD");
        }
    }

    public static DatabaseAction updateRemoveScheduledParty(String id, String party) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "scheduledParties", new AttributeValue(party), false,
                "DELETE");
    }

    public static DatabaseAction updateAddCompletedParty(String id, String party) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedParties", new AttributeValue(party), false,
                "ADD");
    }

    public static DatabaseAction updateRemoveCompletedParty(String id, String party) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedChallenges", new AttributeValue(party), false,
                "DELETE");
    }

    public static DatabaseAction updateAddScheduledChallenge(String id, String challenge, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "scheduledChallenges", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "scheduledChallenges", new AttributeValue(challenge),
                    true, "ADD");
        }
    }

    public static DatabaseAction updateRemoveScheduledChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "scheduledChallenges", new AttributeValue(challenge), false,
                "DELETE");
    }

    public static DatabaseAction updateAddCompletedChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedChallenges", new AttributeValue(challenge), false,
                "ADD");
    }

    public static DatabaseAction updateRemoveCompletedChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completedChallenges", new AttributeValue(challenge), false,
                "DELETE");
    }

    public static DatabaseAction updateAddOwnedParty(String id, String party, boolean ifWithCreate) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "ownedParties", new AttributeValue(party), ifWithCreate,
                "ADD");
    }

    public static DatabaseAction updateRemoveOwnedParty(String id, String party) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "ownedChallenges", new AttributeValue(party), false,
                "DELETE");
    }

    public static DatabaseAction updateAddOwnedChallenge(String id, String challenge, boolean ifWithCreate) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "ownedChallenges", new AttributeValue(challenge), ifWithCreate,
                "ADD");
    }

    public static DatabaseAction updateRemoveOwnedChallenge(String id, String challenge) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "ownedChallenges", new AttributeValue(challenge), false,
                "DELETE");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Client"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
