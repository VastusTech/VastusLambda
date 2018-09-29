package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseObjects.Trainer;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateTrainerRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainerDatabaseActionBuilder {
    final static private String itemType = "Trainer";

    public static DatabaseAction create(CreateTrainerRequest createTrainerRequest) {
        // Handle the setting of the items!
        Map<String, AttributeValue> item = Trainer.getEmptyItem();
        item.put("name", new AttributeValue(createTrainerRequest.name));
        item.put("gender", new AttributeValue(createTrainerRequest.gender));
        item.put("birthday", new AttributeValue(createTrainerRequest.birthday));
        item.put("email", new AttributeValue(createTrainerRequest.email));
        item.put("username", new AttributeValue(createTrainerRequest.username));
        item.put("gymID", new AttributeValue(createTrainerRequest.gymID));
        item.put("workout_sticker", new AttributeValue(createTrainerRequest.workoutSticker));
        item.put("preferred_intensity", new AttributeValue(createTrainerRequest.preferredIntensity));
        if (createTrainerRequest.bio != null) { item.put("bio", new AttributeValue(createTrainerRequest.bio)); }
        if (createTrainerRequest.workoutCapacity != null) { item.put("workout_capacity", new AttributeValue
                (createTrainerRequest.workoutCapacity)); }
        if (createTrainerRequest.workoutPrice != null) { item.put("workout_price", new AttributeValue
                (createTrainerRequest.workoutPrice)); }
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
                Trainer trainer = (Trainer)newObject;
                // Is it during one of the trainer's available times?
                for (TimeInterval availableTime : trainer.availableTimes) {
                    if (availableTime.encompasses(workoutTimeInterval)) {
                        // Then, is it conflicting with another one of their workouts?
                        for (TimeInterval trainerWorkoutTime : trainer.scheduledWorkoutTimes) {
                            if (trainerWorkoutTime.intersects(workoutTimeInterval)) {
                                return false;
                            }
                        }

                        return true;
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

//    public static DatabaseAction updateGymID(String id, String gymID) throws Exception {
//        return new UpdateDatabaseAction();
//    }

    public static DatabaseAction updateAddAvailableTimes(String id, String[] availableTimes) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "available_times", new AttributeValue(Arrays.asList
                (availableTimes)), false, "ADD");
    }

    public static DatabaseAction updateRemoveAvailableTimes(String id, String[] availableTimes) throws Exception {
        List<TimeInterval> availableTimeIntervals = TimeInterval.getTimeIntervals(Arrays.asList(availableTimes));
        return new UpdateDatabaseAction(id, itemType, "available_times", new AttributeValue(Arrays.asList
                (availableTimes)), false, "REMOVE", new CheckHandler() {
            @Override
            public boolean isViable(DatabaseObject newObject) throws Exception {
                Trainer trainer = (Trainer)newObject;
                for (TimeInterval workoutTime : trainer.scheduledWorkoutTimes) {
                    for (TimeInterval availableTimeInterval : availableTimeIntervals) {
                        if (availableTimeInterval.intersects(workoutTime)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });
    }

    public static DatabaseAction updateWorkoutSticker(String id, String sticker) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "workout_sticker", new AttributeValue(sticker), false, "PUT");
    }

    public static DatabaseAction updatePreferredIntensity(String id, String intensity) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "preferred_intensity", new AttributeValue(intensity), false,
                "PUT");
    }

    public static DatabaseAction updateWorkoutCapacity(String id, String capacity) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "workout_capacity", new AttributeValue(capacity), false, "PUT");
    }

    public static DatabaseAction updateWorkoutPrice(String id, String price) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "workout_price", new AttributeValue(price), false, "PUT");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Trainer"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
