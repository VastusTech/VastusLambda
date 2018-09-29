package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import main.java.Logic.Constants;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.joda.time.DateTime;
import org.joda.time.Years;

import java.util.*;

abstract public class User extends DatabaseObject{
    public String name;
    public String gender;
    public String birthday;
    public int age;
    public String email;
    public String username;
    public String profileImagePath;
    public Set<String> scheduledWorkouts;
    public Set<String> completedWorkouts;
    public List<TimeInterval> scheduledWorkoutTimes;
    public List<TimeInterval> completedWorkoutTimes;
    public Set<String> reviewsAbout;
    public Set<String> reviewsBy;
    public float friendlinessRating;
    public float effectivenessRating;
    public float reliabilityRating;
    public float overallRating;
    public String bio;

    public User(Item item) throws Exception {
        super(item);
        this.name = item.getString("name");
        this.gender = item.getString("gender");
        this.birthday = item.getString("birthday");
        this.age = getAgeFromBirthday(birthday);
        this.email = item.getString("email");
        this.username = item.getString("username");
        this.profileImagePath = item.getString("profile_image_path");
        Set<String> scheduledWorkouts = item.getStringSet("scheduled_workouts");
        if (scheduledWorkouts != null) { this.scheduledWorkouts = scheduledWorkouts; }
        else { this.scheduledWorkouts = new HashSet<>(); }
        Set<String> completedWorkouts = item.getStringSet("completed_workouts");
        if (completedWorkouts != null) { this.completedWorkouts = completedWorkouts; }
        else { this.completedWorkouts = new HashSet<>(); }
        Set<String> scheduledWorkoutTimes = item.getStringSet("scheduled_workout_times");
        if (scheduledWorkoutTimes != null) { this.scheduledWorkoutTimes = TimeInterval.getTimeIntervals(scheduledWorkoutTimes); }
        else { this.scheduledWorkoutTimes = new ArrayList<>(); }
        Set<String> completedWorkoutTimes = item.getStringSet("completed_workout_times");
        if (completedWorkoutTimes != null) { this.completedWorkoutTimes = TimeInterval.getTimeIntervals(completedWorkoutTimes); }
        else { this.completedWorkoutTimes = new ArrayList<>(); }
        Set<String> reviewsBy = item.getStringSet("reviews_by");
        if (reviewsBy != null) { this.reviewsBy = reviewsBy; }
        else { this.reviewsBy = new HashSet<>(); }
        Set<String> reviewsAbout = item.getStringSet("reviews_about");
        if (reviewsAbout != null) { this.reviewsAbout = reviewsAbout; }
        else { this.reviewsAbout = new HashSet<>(); }
        this.friendlinessRating = Float.parseFloat(item.getString("friendliness_rating"));
        this.effectivenessRating = Float.parseFloat(item.getString("effectiveness_rating"));
        this.reliabilityRating = Float.parseFloat(item.getString("reliability_rating"));
        this.overallRating = (friendlinessRating + effectivenessRating + reliabilityRating) / 3.0f;
        this.bio = item.getString("bio");
    }

    private int getAgeFromBirthday(String birthday) {
        DateTime currentDateTime = new DateTime();
        DateTime birthdayDateTime = new DateTime(birthday);
        return Years.yearsBetween(birthdayDateTime, currentDateTime).getYears();
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        // item.put("name", new AttributeValue(Constants.nullAttributeValue));
        item.put("name", null);
        // item.put("gender", new AttributeValue(Constants.nullAttributeValue));
        item.put("gender", null);
        // item.put("birthday", new AttributeValue(Constants.nullAttributeValue));
        item.put("birthday", null);
        // item.put("email", new AttributeValue(Constants.nullAttributeValue));
        item.put("email", null);
        // item.put("username", new AttributeValue(Constants.nullAttributeValue));
        item.put("username", null);
        // item.put("profile_image_path", new AttributeValue(Constants.nullAttributeValue));
        item.put("profile_image_path", null);
        item.put("scheduled_workouts", null);
        item.put("completed_workouts", null);
        item.put("scheduled_workout_times", null);
        item.put("completed_workout_times", null);
        item.put("reviews_by", null);
        item.put("reviews_about", null);
        item.put("friendliness_rating", new AttributeValue("0.0"));
        item.put("effectiveness_rating", new AttributeValue("0.0"));
        item.put("reliability_rating", new AttributeValue("0.0"));
        // item.put("bio", new AttributeValue(Constants.nullAttributeValue));
        item.put("bio", null);
        return item;
    }
}
