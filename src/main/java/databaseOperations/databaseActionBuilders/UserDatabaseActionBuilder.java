package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
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

    public static DatabaseAction updateAddScheduledTime(String id, String itemType, String time,
                                                               CheckHandler checkHandler) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "scheduledTimes", new AttributeValue(time),
                false, "ADD", checkHandler);
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
}
