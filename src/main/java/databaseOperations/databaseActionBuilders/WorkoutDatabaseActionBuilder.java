package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.Workout;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateWorkoutRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WorkoutDatabaseActionBuilder {
    final static private String itemType = "Workout";

    public static DatabaseAction create(CreateWorkoutRequest createWorkoutRequest) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Workout.getEmptyItem();
        item.put("time", new AttributeValue(createWorkoutRequest.time));
        item.put("trainerID", new AttributeValue(createWorkoutRequest.trainerID));
        item.put("clientIDs", new AttributeValue(Arrays.asList(createWorkoutRequest.clientIDs)));
        item.put("missing_reviews", new AttributeValue(Arrays.asList(createWorkoutRequest.clientIDs)));
        item.put("capacity", new AttributeValue(createWorkoutRequest.capacity));
        item.put("gymID", new AttributeValue(createWorkoutRequest.gymID));
        item.put("sticker", new AttributeValue(createWorkoutRequest.sticker));
        item.put("intensity", new AttributeValue(createWorkoutRequest.intensity));
        item.put("price", new AttributeValue(createWorkoutRequest.price));
        return new CreateDatabaseAction(item);
    }

//    public static DatabaseAction updateTime() {
//        return null;
//    }

//    public static DatabaseAction updateTrainerID() {
//        return null;
//    }

    public static DatabaseAction updateAddClientID(String id, String clientID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "clientIDs", new AttributeValue(clientID), false, "ADD", new CheckHandler() {
            @Override
            public boolean isViable(DatabaseObject newObject) throws Exception {
                // The capacity for the workout must not be filled up yet.
                Workout workout = (Workout)newObject;
                return (workout.capacity > workout.clientIDs.size());
            }
        });
    }

    public static DatabaseAction updateRemoveClientID(String id, String clientID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "clientIDs", new AttributeValue(clientID), false, "REMOVE");
    }

//    public static DatabaseAction updateCapacity() {
//        return null;
//    }

//    public static DatabaseAction updateGymID() {
//        return null;
//    }

//    public static DatabaseAction updateSticker() {
//        return null;
//    }

//    public static DatabaseAction updateIntensity() {
//        return null;
//    }

    public static DatabaseAction updateAddMissingReview(String id, String userID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "missing_reviews", new AttributeValue(userID), false, "ADD");
    }

    public static DatabaseAction updateRemoveMissingReview(String id, String userID, boolean ifFinishing) throws
            Exception {
        // If you're finishing a workout, then you need to abide by the rules, but if you're cancelling you don't
        if (ifFinishing) {
            return new UpdateDatabaseAction(id, itemType, "missing_reviews", new AttributeValue(userID), false,
                    "REMOVE", new CheckHandler() {
                @Override
                // You can't do a review if the workout hasn't started yet!!!
                public boolean isViable(DatabaseObject newObject) throws Exception {
                    return ((Workout) newObject).time.hasAlreadyStarted();
                }
            });
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "missing_reviews", new AttributeValue(userID), false,
                    "REMOVE");
        }
    }

//    public static DatabaseAction updatePrice() {
//        return null;
//    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Workout"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }

    public static DatabaseAction deleteIfEmpty(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Workout"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key, new CheckHandler() {
            @Override
            public boolean isViable(DatabaseObject newObject) throws Exception {
                // We only want to delete this object if it is currently empty!
                return ((Workout)newObject).clientIDs.size() == 0;
            }
        });
    }
}
