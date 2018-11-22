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
        item.put("trainer", new AttributeValue(createWorkoutRequest.trainer));
        item.put("clients", new AttributeValue(Arrays.asList(createWorkoutRequest.clients)));
        item.put("missingReviews", new AttributeValue(Arrays.asList(createWorkoutRequest.clients)));
        item.put("capacity", new AttributeValue(createWorkoutRequest.capacity));
        item.put("gym", new AttributeValue(createWorkoutRequest.gym));
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
    public static DatabaseAction updateIfCompleted(String id, String ifCompleted) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "ifCompleted", new AttributeValue(ifCompleted), false, "PUT");
    }

    public static DatabaseAction updateAddClient(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "clients", new AttributeValue(client), false, "ADD", new CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                // The capacity for the workout must not be filled up yet.
                Workout workout = (Workout)newObject;
                if (workout.capacity > workout.clients.size()) {
                    return null;
                }
                else {
                    return "That workout is already filled up!";
                }
            }
        });
    }

    public static DatabaseAction updateRemoveClient(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "clients", new AttributeValue(client), false, "DELETE");
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

    public static DatabaseAction updateAddMissingReview(String id, String user) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "missingReviews", new AttributeValue(user), false, "ADD");
    }

    public static DatabaseAction updateRemoveMissingReview(String id, String user, boolean ifFinishing) throws
            Exception {
        // If you're finishing a workout, then you need to abide by the rules, but if you're cancelling you don't
        if (ifFinishing) {
            return new UpdateDatabaseAction(id, itemType, "missingReviews", new AttributeValue(user), false,
                    "DELETE", new CheckHandler() {
                @Override
                // You can't do a review if the workout hasn't started yet!!!
                public String isViable(DatabaseObject newObject) throws Exception {
                    if (((Workout) newObject).time.hasAlreadyStarted()) {
                        return null;
                    }
                    else {
                        return "Time for the workout hasn't started yet, you can't complete a review yet!";
                    }
                }
            });
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "missingReviews", new AttributeValue(user), false,
                    "DELETE");
        }
    }

//    public static DatabaseAction updatePrice() {
//        return null;
//    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }

    public static DatabaseAction deleteIfEmpty(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key, new CheckHandler() {
            @Override
            public String isViable(DatabaseObject newObject) throws Exception {
                // We only want to delete this object if it is currently empty!
                if (((Workout)newObject).clients.size() == 0) {
                    return null;
                }
                else {
                    return "Can't delete the workout because it's not empty yet....";
                }
            }
        });
    }
}
