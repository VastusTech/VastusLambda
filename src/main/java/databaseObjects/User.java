package main.java.databaseObjects;

import main.java.Logic.Constants;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
        this.scheduledWorkouts = new HashSet<>(item.get("scheduled_workouts").getSS());
        this.completedWorkouts = new HashSet<>(item.get("completed_workouts").getSS());
        this.scheduledWorkoutTimes = TimeInterval.getTimeIntervals(item.get("scheduled_workout_times").getSS());
        this.completedWorkoutTimes = TimeInterval.getTimeIntervals(item.get("completed_workout_times").getSS());
        this.reviewsBy = new HashSet<>(item.get("reviews_by").getSS());
        this.reviewsAbout = new HashSet<>(item.get("reviews_about").getSS());
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
//        item.put("scheduled_workouts", new AttributeValue(new ArrayList<>()));
//        item.put("completed_workouts", new AttributeValue(new ArrayList<>()));
//        item.put("scheduled_workout_times", new AttributeValue(new ArrayList<>()));
//        item.put("completed_workout_times", new AttributeValue(new ArrayList<>()));
//        item.put("reviews_by", new AttributeValue(new ArrayList<>()));
//        item.put("reviews_about", new AttributeValue(new ArrayList<>()));
        item.put("scheduled_workouts", null);
        item.put("completed_workouts", null);
        item.put("scheduled_workout_times", null);
        item.put("completed_workout_times", null);
        item.put("reviews_by", null);
        item.put("reviews_about", null);
        item.put("friendliness_rating", new AttributeValue("-1.0"));
        item.put("effectiveness_rating", new AttributeValue("-1.0"));
        item.put("reliability_rating", new AttributeValue("-1.0"));
        item.put("bio", new AttributeValue(Constants.nullAttributeValue));
        return item;
    }
}
