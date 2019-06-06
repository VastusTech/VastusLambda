package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.DatabaseItem;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.Workout;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateWorkoutRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

/**
 * The Database Action Builder for the {@link Workout} object, getting the {@link DatabaseAction} objects
 * that dictate the individual actions to do in the database for Workouts.
 */
public class WorkoutDatabaseActionBuilder {
    final static private String itemType = "Workout";

    /**
     * Gets the {@link PrimaryKey} object to identify the object in the database.
     *
     * @param id The ID of the object to reference.
     * @return The {@link PrimaryKey} object to identify the database item with.
     */
    static private PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateWorkoutRequest createWorkoutRequest, Map<String, String> passoverIdentifiers) {
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
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
            (Map<String, AttributeValue> createdItem, String id) -> {
                return;
            }
        );
    }

//    public static DatabaseAction updateTime() {
//        return null;
//    }

//    public static DatabaseAction updateTrainerID() {
//        return null;
//    }
    public static DatabaseAction updateIfCompleted(String id, String ifCompleted) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "ifCompleted", new AttributeValue(ifCompleted), false, PUT);
    }

    public static DatabaseAction updateAddClient(String id, String client) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "clients", new AttributeValue(client), false, ADD, new CheckHandler() {
            @Override
            public String isViable(DatabaseItem newItem) throws Exception {
                // The capacity for the workout must not be filled up yet.
                Workout workout = (Workout) newItem;
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
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "clients", new AttributeValue(client), false, DELETE);
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
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "missingReviews", new AttributeValue(user), false, ADD);
    }

    public static DatabaseAction updateRemoveMissingReview(String id, String user, boolean ifFinishing) throws
            Exception {
        // If you're finishing a workout, then you need to abide by the rules, but if you're cancelling you don't
        if (ifFinishing) {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "missingReviews", new AttributeValue(user), false,
                    DELETE, new CheckHandler() {
                @Override
                // You can't do a review if the workout hasn't started yet!!!
                public String isViable(DatabaseItem newItem) throws Exception {
                    if (((Workout) newItem).time.hasAlreadyStarted()) {
                        return null;
                    }
                    else {
                        return "Time for the workout hasn't started yet, you can't complete a review yet!";
                    }
                }
            });
        }
        else {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "missingReviews", new AttributeValue(user), false,
                    DELETE);
        }
    }

//    public static DatabaseAction updatePrice() {
//        return null;
//    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }

    public static DatabaseAction deleteIfEmpty(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id), new CheckHandler() {
            @Override
            public String isViable(DatabaseItem newItem) throws Exception {
                // We only want to delete this object if it is currently empty!
                if (((Workout) newItem).clients.size() == 0) {
                    return null;
                }
                else {
                    return "Can't delete the workout because it's not empty yet....";
                }
            }
        });
    }
}
