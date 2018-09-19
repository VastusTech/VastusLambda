package databaseObjects;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class User extends DatabaseObject{
    public String name;
    public String gender;
    public String birthday;
    public int age;
    public String email;
    public String profileImagePath;

    public List<String> scheduledWorkouts;
    public List<String> completedWorkouts;

    public List<String> reviewsAbout;
    public List<String> reviewsBy;

    public User(String id, String itemType, String marker, String timeCreated,
                String name, String gender, String birthday, String email, String profileImagePath,
                List<String> scheduledWorkouts, List<String> completedWorkouts, List<String> reviewsAbout,
                List<String> reviewsBy, Map<String, AttributeValue> attributes) {
        super(id, itemType, marker, timeCreated, attributes);
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        // TODO Figure out age from the birthday
        this.email = email;
        this.profileImagePath = profileImagePath;
        this.scheduledWorkouts = scheduledWorkouts;
        this.completedWorkouts = completedWorkouts;
        this.reviewsAbout = reviewsAbout;
        this.reviewsBy = reviewsBy;
    }

    public User(Map<String, AttributeValue> item) {
        super(item);
        // TODO Set the variables here
    }
}
