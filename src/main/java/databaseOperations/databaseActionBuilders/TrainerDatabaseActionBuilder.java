package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.DatabaseItem;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseObjects.Trainer;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateTrainerRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class TrainerDatabaseActionBuilder {
    final static private String itemType = "Trainer";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateTrainerRequest createTrainerRequest) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Trainer.getEmptyItem();
        item.put("name", new AttributeValue(createTrainerRequest.name));
        item.put("email", new AttributeValue(createTrainerRequest.email));
        item.put("username", new AttributeValue(createTrainerRequest.username));
        if (createTrainerRequest.gender != null) { item.put("gender", new AttributeValue(createTrainerRequest
                .gender)); }
        if (createTrainerRequest.birthday != null) { item.put("birthday", new AttributeValue(createTrainerRequest
                .birthday)); }
        if (createTrainerRequest.stripeID != null) { item.put("stripeID", new AttributeValue(createTrainerRequest
                .stripeID)); }
        if (createTrainerRequest.federatedID != null) { item.put("federatedID", new AttributeValue(createTrainerRequest
                .federatedID)); }
        if (createTrainerRequest.gym != null) { item.put("gym", new AttributeValue(createTrainerRequest.gym)); }
        if (createTrainerRequest.workoutSticker != null) { item.put("workoutSticker", new AttributeValue
                (createTrainerRequest.workoutSticker)); }
        if (createTrainerRequest.preferredIntensity != null) { item.put("preferredIntensity", new AttributeValue
                (createTrainerRequest.preferredIntensity)); }
        if (createTrainerRequest.bio != null) { item.put("bio", new AttributeValue(createTrainerRequest.bio)); }
        if (createTrainerRequest.workoutCapacity != null) { item.put("workoutCapacity", new AttributeValue
                (createTrainerRequest.workoutCapacity)); }
        if (createTrainerRequest.workoutPrice != null) { item.put("workoutPrice", new AttributeValue
                (createTrainerRequest.workoutPrice)); }
        return new CreateDatabaseAction(itemType, item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    // TODO How to combine this with the original UserActionDatabaseBuilder logic???? "Extends"?
    public static DatabaseAction updateAddScheduledTime(String id, String time, boolean ifSchedulingWorkout) throws
            Exception {
        if (ifSchedulingWorkout) {
            TimeInterval timeInterval = new TimeInterval(time);
            return UserDatabaseActionBuilder.updateAddScheduledTime(id, itemType, time, new CheckHandler() {
                @Override
                public String isViable(DatabaseItem newItem) throws Exception {
                    // This is to check whether any times conflict
                    Trainer trainer = (Trainer) newItem;
                    // Is it during one of the trainer's available times?
                    for (TimeInterval availableTime : trainer.availableTimes) {
                        if (availableTime.encompasses(timeInterval)) {
                            // Then, is it conflicting with another one of their workouts?
                            for (TimeInterval trainerTime : trainer.scheduledTimes) {
                                if (trainerTime.intersects(timeInterval)) {
                                    return "The scheduled time intersects with the trainer's existing schedule!";
                                }
                            }

                            return null;
                        }
                    }
                    return "That time is not during any of the trainer's available times!";
                }
            });
        }
        else {
            return UserDatabaseActionBuilder.updateAddScheduledTime(id, itemType, time, null);
        }
    }

    public static DatabaseAction updateAddAvailableTime(String id, String availableTime) throws Exception {
        TimeInterval timeInterval = new TimeInterval(availableTime);
        return new UpdateDatabaseAction(getPrimaryKey(id), "availableTimes", new AttributeValue(availableTime), false, ADD, new CheckHandler() {
            @Override
            public String isViable(DatabaseItem newItem) throws Exception {
                // Check to see if the available time intersects with any of the scheduled times
                for (TimeInterval scheduledTime : ((Trainer) newItem).scheduledTimes) {
                    if (scheduledTime.intersects(timeInterval)) {
                        return "That available time intersects with your existing schedule!";
                    }
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateRemoveAvailableTime(String id, String availableTime) throws Exception {
        TimeInterval timeInterval = new TimeInterval(availableTime);
        return new UpdateDatabaseAction(getPrimaryKey(id), "availableTimes", new AttributeValue(availableTime), false, DELETE, new CheckHandler() {
            @Override
            public String isViable(DatabaseItem newItem) throws Exception {
                Trainer trainer = (Trainer) newItem;
                for (TimeInterval time : trainer.scheduledTimes) {
                    if (timeInterval.intersects(time)) {
                        return "Trainer already has something scheduled for that time, cannot remove available " +
                                "time!";
                    }
                }
                return null;
            }
        });
    }

    public static DatabaseAction updateWorkoutSticker(String id, String sticker) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "workoutSticker", new AttributeValue(sticker), false, PUT);
    }

    public static DatabaseAction updatePreferredIntensity(String id, String intensity) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "preferredIntensity", new AttributeValue(intensity), false,
                PUT);
    }

    public static DatabaseAction updateWorkoutCapacity(String id, String capacity) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "workoutCapacity", new AttributeValue(capacity), false, PUT);
    }

    public static DatabaseAction updateWorkoutPrice(String id, String price) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "workoutPrice", new AttributeValue(price), false, PUT);
    }

    public static DatabaseAction updateAddSubscriber(String id, String subscriber) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "subscribers", new AttributeValue(subscriber), false, ADD);
    }

    public static DatabaseAction updateRemoveSubscriber(String id, String subscriber) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "subscribers", new AttributeValue(subscriber), false, DELETE);
    }

    public static DatabaseAction updateSubscriptionPrice(String id, String price) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "subscriptionPrice", new AttributeValue(price), false, PUT);
    }

    public static DatabaseAction updateAddCertification(String id, String certification) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "certifications", new AttributeValue(certification), false,
                ADD);
    }

    public static DatabaseAction updateRemoveCertification(String id, String certification) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "certifications", new AttributeValue(certification), false,
                DELETE);
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(itemType, getPrimaryKey(id));
    }
}
