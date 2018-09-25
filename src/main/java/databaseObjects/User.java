package main.java.databaseObjects;

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

    public User(Map<String, AttributeValue> item) throws Exception {
        super(item);
        this.name = item.get("name").getS();
        this.gender = item.get("gender").getS();
        this.birthday = item.get("birthday").getS();
        this.age = getAgeFromBirthday(birthday);
        this.email = item.get("email").getS();
        this.username = item.get("username").getS();
        this.profileImagePath = item.get("profile_image_path").getS();
        AttributeValue scheduledWorkouts = item.get("scheduled_workouts");
        if (scheduledWorkouts != null) { this.scheduledWorkouts = new HashSet<>(scheduledWorkouts.getSS()); }
        else { this.scheduledWorkouts = new HashSet<>(); }
        AttributeValue completedWorkouts = item.get("completed_workouts");
        if (completedWorkouts != null) { this.completedWorkouts = new HashSet<>(completedWorkouts.getSS()); }
        else { this.completedWorkouts = new HashSet<>(); }
        AttributeValue scheduledWorkoutTimes = item.get("scheduled_workout_times");
        if (scheduledWorkoutTimes != null) { this.scheduledWorkoutTimes = TimeInterval.getTimeIntervals(scheduledWorkoutTimes.getSS()); }
        else { this.scheduledWorkoutTimes = new ArrayList<>(); }
        AttributeValue completedWorkoutTimes = item.get("completed_workout_times");
        if (completedWorkoutTimes != null) { this.completedWorkoutTimes = TimeInterval.getTimeIntervals(completedWorkoutTimes.getSS()); }
        else { this.completedWorkoutTimes = new ArrayList<>(); }
        AttributeValue reviewsBy = item.get("reviews_by");
        if (reviewsBy != null) { this.reviewsBy = new HashSet<>(reviewsBy.getSS()); }
        else { this.reviewsBy = new HashSet<>(); }
        AttributeValue reviewsAbout = item.get("reviews_about");
        if (reviewsAbout != null) { this.reviewsAbout = new HashSet<>(reviewsAbout.getSS()); }
        else { this.reviewsAbout = new HashSet<>(); }
        this.friendlinessRating = Float.parseFloat(item.get("friendliness_rating").getS());
        this.effectivenessRating = Float.parseFloat(item.get("effectiveness_rating").getS());
        this.reliabilityRating = Float.parseFloat(item.get("reliability_rating").getS());
        this.overallRating = (friendlinessRating + effectivenessRating + reliabilityRating) / 3.0f;
        this.bio = item.get("bio").getS();
    }

    private int getAgeFromBirthday(String birthday) {
        DateTime currentDateTime = new DateTime();
        DateTime birthdayDateTime = new DateTime(birthday);
        return Years.yearsBetween(birthdayDateTime, currentDateTime).getYears();
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("name", new AttributeValue(Constants.nullAttributeValue));
        item.put("gender", new AttributeValue(Constants.nullAttributeValue));
        item.put("birthday", new AttributeValue(Constants.nullAttributeValue));
        item.put("email", new AttributeValue(Constants.nullAttributeValue));
        item.put("username", new AttributeValue(Constants.nullAttributeValue));
        item.put("profile_image_path", new AttributeValue(Constants.nullAttributeValue));
        item.put("scheduled_workouts", null);
        item.put("completed_workouts", null);
        item.put("scheduled_workout_times", null);
        item.put("completed_workout_times", null);
        item.put("reviews_by", null);
        item.put("reviews_about", null);
        item.put("friendliness_rating", new AttributeValue("0.0"));
        item.put("effectiveness_rating", new AttributeValue("0.0"));
        item.put("reliability_rating", new AttributeValue("0.0"));
        item.put("bio", new AttributeValue(Constants.nullAttributeValue));
        return item;
    }
}
