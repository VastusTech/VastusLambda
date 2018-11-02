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
        Map<String, AttributeValue> item = Gym.getEmptyItem();
        item.put("name", new AttributeValue(createGymRequest.name));
        item.put("birthday", new AttributeValue(createGymRequest.foundingDay));
        item.put("email", new AttributeValue(createGymRequest.email));
        item.put("username", new AttributeValue(createGymRequest.username));
        item.put("address", new AttributeValue(createGymRequest.address));
        item.put("sessionCapacity", new AttributeValue(createGymRequest.sessionCapacity));
        if (createGymRequest.bio != null) { item.put("bio", new AttributeValue(createGymRequest.bio)); }
        if (createGymRequest.weeklyHours != null) { item.put("weeklyHours", new AttributeValue(Arrays.asList
                (createGymRequest.weeklyHours))); }
        if (createGymRequest.gymType != null) { item.put("gymType",
                new AttributeValue(createGymRequest.gymType)); }
        if (createGymRequest.paymentSplit != null) { item.put("paymentSplit",
                new AttributeValue(createGymRequest.paymentSplit)); }
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
                Gym gym = (Gym)newObject;

                // Check every time section to see if it's too filled for the gym space.
                for (TimeInterval timeSection : timeInterval.getAllTimeSections()) {
                    int numWorkouts = 0;
                    for (TimeInterval gymTime : gym.scheduledTimes) {
                        if (timeSection.intersects(gymTime)) {
                            numWorkouts++;
                        }
                    }

                    // If the number of workouts is at (or above lol) the capacity, then you can't add another one
                    if (numWorkouts >= gym.sessionCapacity) {
                        return "Gym is already at capacity for that time!";
                    }
                }

                // Has to be during the weekly time
                for (TimeInterval weeklyTime : gym.weeklyHours) {
                    if (weeklyTime.weeklyEncompasses(timeInterval)) {
                        // Then make sure that it isn't during a vacation time
                        for (TimeInterval vacationTime : gym.vacationTimes) {
                            if (vacationTime.intersects(timeInterval)) {
                                return "That workout is during one of the gym's vacation times!";
                            }
                        }

                        return null;
                    }
                }

                return "That workout isn't during the gym's open hours!";
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

    public static DatabaseAction updateAddress(String id, String address) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "address", new AttributeValue(address), false, "PUT");
    }

    public static DatabaseAction updateAddTrainer(String id, String trainer, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "trainer", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "trainer", new AttributeValue(trainer), false, "ADD");
        }

    }

    public static DatabaseAction updateRemoveTrainer(String id, String trainer) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "trainer", new AttributeValue(trainer), false, "DELETE");
    }

    public static DatabaseAction updateWeeklyHours(String id, String[] weeklyHours) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "weeklyHours", new AttributeValue(Arrays.asList(weeklyHours)),
                false, "PUT");
    }

//    public static DatabaseAction updateAddWeeklyHour(String id, String weeklyHour) throws Exception {
//        return new UpdateDatabaseAction(id, itemType, "weekly_hours", new AttributeValue(weeklyHour), false, "ADD");
//    }
//
//    public static DatabaseAction updateRemoveWeeklyHour(String id, String weeklyHour) throws Exception {
//        return new UpdateDatabaseAction(id, itemType, "weekly_hours", new AttributeValue(weeklyHour), false, "REMOVE");
//    }

    public static DatabaseAction updateAddVacationTimes(String id, String[] vacationTimes) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "vacationTimes", new AttributeValue(Arrays.asList
                (vacationTimes)), false, "ADD");
    }

    public static DatabaseAction updateRemoveVacationTimes(String id, String[] vacationTimes) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "vacationTimes", new AttributeValue(Arrays.asList
                (vacationTimes)), false, "DELETE");
    }

    public static DatabaseAction updateSessionCapacity(String id, String sessionCapacity) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "sessionCapacity", new AttributeValue(sessionCapacity), false,
                "PUT");
    }

    public static DatabaseAction updateGymType(String id, String gymType) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "gymType", new AttributeValue(gymType), false, "PUT");
    }

    public static DatabaseAction updatePaymentSplit(String id, String paymentSplit) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "paymentSplit", new AttributeValue(paymentSplit), false, "PUT");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Gym"));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}

