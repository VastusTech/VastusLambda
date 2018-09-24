package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.DatabaseObject;
import main.java.databaseObjects.Gym;
import main.java.databaseObjects.TimeInterval;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateGymRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GymDatabaseActionBuilder {
    final static private String itemType = "Gym";

    public static DatabaseAction create(CreateGymRequest createGymRequest) {
        // Handle the setting of the items!
        // TODO Make sure that the null values aren't too too problematic
        Map<String, AttributeValue> item = Gym.getEmptyItem();
        item.put("name", new AttributeValue(createGymRequest.name));
        item.put("birthday", new AttributeValue(createGymRequest.foundingDay));
        item.put("email", new AttributeValue(createGymRequest.email));
        item.put("username", new AttributeValue(createGymRequest.username));
        item.put("address", new AttributeValue(createGymRequest.address));
        item.put("weekly_hours", new AttributeValue(Arrays.asList(createGymRequest.weeklyHours)));
        item.put("session_capacity", new AttributeValue(createGymRequest.sessionCapacity));
        item.put("gym_type", new AttributeValue(createGymRequest.gymType));
        item.put("payment_split", new AttributeValue(createGymRequest.paymentSplit));
        return new CreateDatabaseAction(item);
    }

    public static DatabaseAction updateName(String id, String name) throws Exception {
        return UserDatabaseActionBuilder.updateName(id, itemType, name);
    }

    public static DatabaseAction updateFoundingDay(String id, String foundingDay) throws Exception {
        return UserDatabaseActionBuilder.updateBirthday(id, itemType, foundingDay);
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
                Gym gym = (Gym)newObject;

                // Check every time section to see if it's too filled for the gym space.
                for (TimeInterval timeSection : workoutTimeInterval.getAllTimeSections()) {
                    int numWorkouts = 0;
                    for (TimeInterval gymWorkoutTime : gym.scheduledWorkoutTimes) {
                        if (timeSection.intersects(gymWorkoutTime)) {
                            numWorkouts++;
                        }
                    }

                    // If the number of workouts is at (or above lol) the capacity, then you can't add another one
                    if (numWorkouts >= gym.sessionCapacity) {
                        return false;
                    }
                }

                // Has to be during the weekly time
                for (TimeInterval weeklyTime : gym.weeklyHours) {
                    if (weeklyTime.weeklyEncompasses(workoutTimeInterval)) {
                        // Then make sure that it isn't during a vacation time
                        for (TimeInterval vacationTime : gym.vacationTimes) {
                            if (vacationTime.intersects(workoutTimeInterval)) {
                                return false;
                            }
                        }

                        return true;
                    }
                }

                return false;
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

    public static DatabaseAction updateAddress(String id, String address) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "address", new AttributeValue(address), false, "SET");
    }

    public static DatabaseAction updateAddTrainerID(String id, String trainerID, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "trainerID", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "trainerID", new AttributeValue(trainerID), false, "ADD");
        }

    }

    public static DatabaseAction updateRemoveTrainerID(String id, String trainerID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "trainerID", new AttributeValue(trainerID), false, "REMOVE");
    }

    public static DatabaseAction updateWeeklyHours(String id, String[] weeklyHours) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "weekly_hours", new AttributeValue(Arrays.asList(weeklyHours)),
                false, "SET");
    }

//    public static DatabaseAction updateAddWeeklyHour(String id, String weeklyHour) throws Exception {
//        return new UpdateDatabaseAction(id, itemType, "weekly_hours", new AttributeValue(weeklyHour), false, "ADD");
//    }
//
//    public static DatabaseAction updateRemoveWeeklyHour(String id, String weeklyHour) throws Exception {
//        return new UpdateDatabaseAction(id, itemType, "weekly_hours", new AttributeValue(weeklyHour), false, "REMOVE");
//    }

    public static DatabaseAction updateAddVacationTime(String id, String vacationTime) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "vacation_times", new AttributeValue(vacationTime), false, "ADD");
    }

    public static DatabaseAction updateRemoveVacationTime(String id, String vacationTime) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "vacation_times", new AttributeValue(vacationTime), false,
                "REMOVE");
    }

    public static DatabaseAction updateSessionCapacity(String id, String sessionCapacity) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "session_capacity", new AttributeValue(sessionCapacity), false,
                "SET");
    }

    public static DatabaseAction updateGymType(String id, String gymType) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "gym_type", new AttributeValue(gymType), false, "SET");
    }

    public static DatabaseAction updatePaymentSplit(String id, String paymentSplit) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "payment_split", new AttributeValue(paymentSplit), false, "SET");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Gym"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}

