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
    public List<TimeInterval> scheduledTimes;
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
        this.profileImagePath = item.getString("profileImagePath");
        this.scheduledWorkouts = item.getStringSet("scheduledWorkouts");
        if (scheduledWorkouts == null) { this.scheduledWorkouts = new HashSet<>(); }
        this.completedWorkouts = item.getStringSet("completedWorkouts");
        if (completedWorkouts == null) { this.completedWorkouts = new HashSet<>(); }
        Set<String> scheduledTimes = item.getStringSet("scheduledTimes");
        if (scheduledTimes != null) { this.scheduledTimes = TimeInterval.getTimeIntervals(scheduledTimes); }
        else { this.scheduledTimes = new ArrayList<>(); }
        this.reviewsBy = item.getStringSet("reviewsBy");
        if (reviewsBy == null) { this.reviewsBy = new HashSet<>(); }
        this.reviewsAbout = item.getStringSet("reviewsAbout");
        if (reviewsAbout == null) { this.reviewsAbout = new HashSet<>(); }
        this.friendlinessRating = Float.parseFloat(item.getString("friendlinessRating"));
        this.effectivenessRating = Float.parseFloat(item.getString("effectivenessRating"));
        this.reliabilityRating = Float.parseFloat(item.getString("reliabilityRating"));
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
        // item.put("name", null);
        // item.put("gender", new AttributeValue(Constants.nullAttributeValue));
        // item.put("gender", null);
        // item.put("birthday", new AttributeValue(Constants.nullAttributeValue));
        // item.put("birthday", null);
        // item.put("email", new AttributeValue(Constants.nullAttributeValue));
        // item.put("email", null);
        // item.put("username", new AttributeValue(Constants.nullAttributeValue));
        // item.put("username", null);
        // item.put("profile_image_path", new AttributeValue(Constants.nullAttributeValue));
        // TODO Point the default image to a default image in the S3 bucket
        // item.put("profile_image_path", null);
        // item.put("scheduled_workouts", null);
        // item.put("completed_workouts", null);
        // item.put("scheduled_times", null);
        // item.put("reviews_by", null);
        // item.put("reviews_about", null);
        item.put("friendlinessRating", new AttributeValue("0.0"));
        item.put("effectivenessRating", new AttributeValue("0.0"));
        item.put("reliabilityRating", new AttributeValue("0.0"));
        // item.put("bio", new AttributeValue(Constants.nullAttributeValue));
        // item.put("bio", null);
        return item;
    }
}
