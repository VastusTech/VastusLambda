package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.databaseOperations.exceptions.CorruptedItemException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A Group is a social hub of people where they can participate in Challenges together and do Events
 * and schedule things. There are multiple owners to a Group and a Group can house Challenges,
 * Events, and Posts.
 */
public class Group extends DatabaseObject {
    public String title;
    public String description;
    public String motto;
    public String groupImagePath;
    public Set<String> owners;
    public Set<String> members;
    public Set<String> invitedMembers;
    public Set<String> memberRequests;
    public Set<String> receivedInvites;
    public String access;
    public String restriction;
    public Set<String> events;
    public Set<String> completedEvents;
    public Set<String> challenges;
    public Set<String> completedChallenges;
    public Set<String> posts;
    public Set<String> tags;
    public Set<String> streaks;

    /**
     * The main constructor for the Group class, instantiating the object from the database.
     *
     * @param item The {@link Item} object obtained from the database query/fetch.
     * @throws Exception If anything goes wrong with the translation.
     */
    public Group(Item item) throws Exception {
        super(item);
        if (!itemType.equals("Group")) throw new CorruptedItemException("Group initialized for wrong item type");
        this.title = item.getString("title");
        this.description = item.getString("description");
        this.motto = item.getString("motto");
        this.groupImagePath = item.getString("groupImagePath");
        this.owners = item.getStringSet("owners");
        if (this.owners == null) { this.owners = new HashSet<>(); }
        this.members = item.getStringSet("members");
        if (this.members == null) { this.members = new HashSet<>(); }
        this.invitedMembers = item.getStringSet("invitedMembers");
        if (this.invitedMembers == null) { this.invitedMembers = new HashSet<>(); }
        this.memberRequests = item.getStringSet("memberRequests");
        if (this.memberRequests == null) { this.memberRequests = new HashSet<>(); }
        this.receivedInvites = item.getStringSet("receivedInvites");
        if (this.receivedInvites == null) { this.receivedInvites = new HashSet<>(); }
        this.access = item.getString("access");
        this.restriction = item.getString("restriction");
        this.events = item.getStringSet("events");
        if (this.events == null) { this.events = new HashSet<>(); }
        this.completedEvents = item.getStringSet("completedEvents");
        if (this.completedEvents == null) { this.completedEvents = new HashSet<>(); }
        this.challenges = item.getStringSet("challenges");
        if (this.challenges == null) { this.challenges = new HashSet<>(); }
        this.completedChallenges = item.getStringSet("completedChallenges");
        if (this.completedChallenges == null) { this.completedChallenges = new HashSet<>(); }
        this.posts = item.getStringSet("posts");
        if (this.posts == null) { this.posts = new HashSet<>(); }
        this.tags = item.getStringSet("tags");
        if (this.tags == null) { this.tags = new HashSet<>(); }
        this.streaks = item.getStringSet("streaks");
        if (this.streaks == null) { this.streaks = new HashSet<>(); }
    }

    /**
     * Gets the empty item with the default values for the Group object.
     *
     * @return The map of attribute values for the item.
     */
    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Group"));
        return item;
    }

    /**
     * Reads a Group from the database using the given ID.
     *
     * TODO Implement cache system here again?
     *
     * @param id The ID to read from the database.
     * @return The Group object to read in the database.
     * @throws Exception If anything goes wrong in the fetch.
     */
    public static Group readGroup(String id) throws Exception {
        return (Group) read(getTableName(), getPrimaryKey("Group", id));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Group) && obj.hashCode() == hashCode()
                && getObjectFieldsList().equals(((Group)obj).getObjectFieldsList());
    }

    @Override
    protected List<Object> getObjectFieldsList() {
        List<Object> list = super.getObjectFieldsList();
        list.addAll(Arrays.asList(owners, members, invitedMembers, memberRequests, receivedInvites,
                events, completedEvents, challenges, completedChallenges, posts, tags, streaks));
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, motto, groupImagePath, access,
                restriction);
    }
}
