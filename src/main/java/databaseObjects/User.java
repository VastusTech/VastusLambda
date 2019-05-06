package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import main.java.logic.ItemType;

import org.joda.time.DateTime;
import org.joda.time.Years;

import java.util.*;

/**
 * A User is an object that represents someone using our application. They have an AWS Cognito User
 * Pool user associated with the object and can use their credentials to log in and access this
 * User. Users can do essentially anything on the platform, like create challenges, events, and
 * groups, post content, invite other Users, and message anyone on the platform.
 */
abstract public class User extends DatabaseObject{
    public String name;
    public String gender;
    public String birthday;
    public int age;
    public String email;
    public String location;
    public String username;
    public String federatedID;
    public String stripeID;
    public String profileImagePath;
    public Set<String> profileImagePaths;
    public Set<String> scheduledWorkouts;
    public Set<String> completedWorkouts;
    public List<TimeInterval> scheduledTimes;
    public Set<String> reviewsBy;
    public Set<String> reviewsAbout;
    public float friendlinessRating;
    public float effectivenessRating;
    public float reliabilityRating;
    public float overallRating;
    public String bio;
    public Set<String> friends;
    public Set<String> friendRequests;
    public Set<String> challenges;
    public Set<String> completedChallenges;
    public Set<String> ownedChallenges;
    public Set<String> invitedChallenges;
    public Set<String> challengesWon;
    public Set<String> scheduledEvents;
    public Set<String> completedEvents;
    public Set<String> ownedEvents;
    public Set<String> invitedEvents;
    public Set<String> sentInvites;
    public Set<String> receivedInvites;
    public Set<String> posts;
    public Set<String> submissions;
    public Set<String> liked;
    public Set<String> comments;
    public Set<String> groups;
    public Set<String> ownedGroups;
    public Set<String> invitedGroups;
    public Set<String> messageBoards;
    public Set<String> streaks;

    /**
     * The main constructor for the User class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public User(Item item) throws Exception {
        super(item);
        this.name = item.getString("name");
        this.gender = item.getString("gender");
        this.birthday = item.getString("birthday");
        this.age = getAgeFromBirthday(birthday);
        this.email = item.getString("email");
        this.location = item.getString("location");
        this.username = item.getString("username");
        this.federatedID = item.getString("federatedID");
        this.stripeID = item.getString("stripeID");
        this.profileImagePath = item.getString("profileImagePath");
        this.profileImagePaths = item.getStringSet("profileImagePaths");
        if (profileImagePaths == null) { this.profileImagePaths = new HashSet<>(); }
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
        this.friends = item.getStringSet("friends");
        if (friends == null) { this.friends = new HashSet<>(); }
        this.friendRequests = item.getStringSet("friendRequests");
        if (friendRequests == null) { this.friendRequests = new HashSet<>(); }
        this.challenges = item.getStringSet("challenges");
        if (challenges == null) { this.challenges = new HashSet<>(); }
        this.completedChallenges = item.getStringSet("completedChallenges");
        if (completedChallenges == null) { this.completedChallenges = new HashSet<>(); }
        this.ownedChallenges = item.getStringSet("ownedChallenges");
        if (ownedChallenges == null) { this.ownedChallenges = new HashSet<>(); }
        this.invitedChallenges = item.getStringSet("invitedChallenges");
        if (invitedChallenges == null) { this.invitedChallenges = new HashSet<>(); }
        this.challengesWon = item.getStringSet("challengesWon");
        if (challengesWon == null) { this.challengesWon = new HashSet<>(); }
        this.scheduledEvents = item.getStringSet("scheduledEvents");
        if (scheduledEvents == null) { this.scheduledEvents = new HashSet<>(); }
        this.completedEvents = item.getStringSet("completedEvents");
        if (completedEvents == null) { this.completedEvents = new HashSet<>(); }
        this.ownedEvents = item.getStringSet("ownedEvents");
        if (ownedEvents == null) { this.ownedEvents = new HashSet<>(); }
        this.invitedEvents = item.getStringSet("invitedEvents");
        if (invitedEvents == null) { this.invitedEvents = new HashSet<>(); }
        this.sentInvites = item.getStringSet("sentInvites");
        if (sentInvites == null) { this.sentInvites = new HashSet<>(); }
        this.receivedInvites = item.getStringSet("receivedInvites");
        if (receivedInvites == null) { this.receivedInvites = new HashSet<>(); }
        this.posts = item.getStringSet("posts");
        if (posts == null) { this.posts = new HashSet<>(); }
        this.submissions = item.getStringSet("submissions");
        if (submissions == null) { this.submissions = new HashSet<>(); }
        this.liked = item.getStringSet("liked");
        if (liked == null) { this.liked = new HashSet<>(); }
        this.comments = item.getStringSet("comments");
        if (comments == null) { this.comments = new HashSet<>(); }
        this.groups = item.getStringSet("groups");
        if (groups == null) { this.groups = new HashSet<>(); }
        this.invitedGroups = item.getStringSet("invitedGroups");
        if (invitedGroups == null) { this.invitedGroups = new HashSet<>(); }
        this.ownedGroups = item.getStringSet("ownedGroups");
        if (ownedGroups == null) { this.ownedGroups = new HashSet<>(); }
        this.messageBoards = item.getStringSet("messageBoards");
        if (messageBoards == null) { this.messageBoards = new HashSet<>(); }
        this.streaks = item.getStringSet("streaks");
        if (streaks == null) { this.streaks = new HashSet<>(); }
    }

    private int getAgeFromBirthday(String birthday) {
        DateTime currentDateTime = new DateTime();
        DateTime birthdayDateTime = new DateTime(birthday);
        return Years.yearsBetween(birthdayDateTime, currentDateTime).getYears();
    }

    /**
     * Gets the empty item with the default values for the User object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("friendlinessRating", new AttributeValue("0.0"));
        item.put("effectivenessRating", new AttributeValue("0.0"));
        item.put("reliabilityRating", new AttributeValue("0.0"));
        return item;
    }

    /**
     * Reads a User from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @param itemType The type of the User to read from the database.
     * @return The User object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static User readUser(String id, String itemType) throws Exception {
        return (User) read(tableName, getPrimaryKey(itemType, id));
    }

    public static User readUser(String id) throws Exception {
        String itemType = ItemType.getItemType(id);
        if (itemType.equals("Client") || itemType.equals("Trainer")
                || itemType.equals("Gym") || itemType.equals("Sponsor")) {
            return readUser(id, itemType);
        }
        else {
            throw new Exception("Item type of non-user found! Type: " + itemType);
        }
    }
}
