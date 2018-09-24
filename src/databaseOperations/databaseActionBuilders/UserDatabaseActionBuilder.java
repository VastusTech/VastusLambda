package databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import databaseObjects.DatabaseObject;
import databaseOperations.CheckHandler;
import databaseOperations.DatabaseAction;
import databaseOperations.UpdateDatabaseAction;

public class UserDatabaseActionBuilder {
    public static DatabaseAction updateName(String id, String itemType, String name) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "name", new AttributeValue(name), false, "SET");
    }

    public static DatabaseAction updateGender(String id, String itemType, String gender) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "gender", new AttributeValue(gender), false, "SET");
    }

    public static DatabaseAction updateBirthday(String id, String itemType, String birthday) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "birthday", new AttributeValue(birthday), false, "SET");
    }

    public static DatabaseAction updateEmail(String id, String itemType, String email) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "email", new AttributeValue(email), false, "SET");
    }

    public static DatabaseAction updateProfileImagePath(String id, String itemType, String profileImagePath) throws
            Exception {
        return new UpdateDatabaseAction(id, itemType, "profile_image_path", new AttributeValue(profileImagePath),
                false, "SET");
    }

    public static DatabaseAction updateAddScheduledWorkout(String id, String itemType, String workoutID, boolean
            ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "scheduled_workouts", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "scheduled_workouts", new AttributeValue(workoutID),
                        true, "ADD");
        }
    }

    public static DatabaseAction updateRemoveScheduledWorkout(String id, String itemType, String workoutID) throws
            Exception {
        // TODO REFUNDS?
        return new UpdateDatabaseAction(id, itemType, "scheduled_workouts", new AttributeValue(workoutID), false, "REMOVE");
    }

    public static DatabaseAction updateAddCompletedWorkout(String id, String itemType, String workoutID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completed_workouts", new AttributeValue(workoutID), false,
                "ADD");
    }

    public static DatabaseAction updateRemoveCompletedWorkout(String id, String itemType, String workoutID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completed_workouts", new AttributeValue(workoutID), false,
                "REMOVE");
    }

    public static DatabaseAction updateAddScheduledWorkoutTime(String id, String itemType, String workoutTime,
                                                               CheckHandler checkHandler) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "scheduled_workout_times", new AttributeValue(workoutTime),
                false, "ADD", checkHandler);
    }

    public static DatabaseAction updateRemoveScheduledWorkoutTime(String id, String itemType, String workoutTime)
            throws Exception {
        return new UpdateDatabaseAction(id, itemType, "scheduled_workout_times", new AttributeValue(workoutTime),
                false, "REMOVE");
    }

    public static DatabaseAction updateAddCompletedWorkoutTime(String id, String itemType, String workoutTime) throws
            Exception {
        return null;
    }

    public static DatabaseAction updateRemoveCompletedWorkoutTime(String id, String itemType, String workoutTime)
            throws Exception {
        return new UpdateDatabaseAction(id, itemType, "completed_workout_times", new AttributeValue(workoutTime),
                false, "REMOVE");
    }

    public static DatabaseAction updateAddReviewBy(String id, String itemType, String reviewID, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "reviews_by", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "reviews_by", new AttributeValue(reviewID),
                        true, "ADD");
        }
    }

    public static DatabaseAction updateRemoveReviewBy(String id, String itemType, String reviewID) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "reviews_by", new AttributeValue(reviewID), false, "REMOVE");
    }

    public static DatabaseAction updateAddReviewAbout(String id, String itemType, String reviewID, boolean ifWithCreate)
            throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "reviews_about", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "reviews_about", new AttributeValue(reviewID),
                        true, "ADD");
        }
    }

    public static DatabaseAction updateRemoveReviewAbout(String id, String itemType, String reviewID) throws
            Exception {
        return new UpdateDatabaseAction(id, itemType, "reviews_about", new AttributeValue(reviewID), false, "REMOVE");
    }

    public static DatabaseAction updateFriendlinessRating(String id, String itemType, String rating) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "friendliness_rating", new AttributeValue(rating), false, "SET");
    }

    public static DatabaseAction updateEffectivenessRating(String id, String itemType, String rating) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "effectiveness_rating", new AttributeValue(rating), false, "SET");
    }

    public static DatabaseAction updateReliabilityRating(String id, String itemType, String rating) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "reliability_rating", new AttributeValue(rating), false, "SET");
    }

    public static DatabaseAction updateBio(String id, String itemType, String bio) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "bio", new AttributeValue(bio), false, "SET");
    }
}
