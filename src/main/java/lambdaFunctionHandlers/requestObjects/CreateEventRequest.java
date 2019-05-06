package main.java.lambdaFunctionHandlers.requestObjects;

/**
 * The POJO for the request if the Lambda caller wants to create a Event in the database.
 */
public class CreateEventRequest extends CreateObjectRequest {
    // Required
    public String owner;
    public String time;
    public String capacity;
    public String address;
    public String title;

    // Optional
    public String description;
    public String[] tags;
    public String[] members;
    public String access;
    public String restriction;
    public String challenge;
    public String group;

    public CreateEventRequest(String owner, String time, String capacity, String address, String title, String
            challenge, String description, String[] tags, String[] members, String access, String restriction, String
             group) {
        this.owner = owner;
        this.time = time;
        this.capacity = capacity;
        this.address = address;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.members = members;
        this.access = access;
        this.restriction = restriction;
        this.challenge = challenge;
        this.group = group;
    }

    public CreateEventRequest() {}

    @Override
    public boolean ifHasEmptyString() {
        return hasEmptyString(owner, time, capacity, address, title, description, access, restriction,
                challenge, group)
                || arrayHasEmptyString(tags, members);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

