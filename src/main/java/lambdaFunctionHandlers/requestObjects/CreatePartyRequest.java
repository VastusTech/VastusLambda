package main.java.lambdaFunctionHandlers.requestObjects;

public class CreatePartyRequest {
    // Required
    public String owner;
    public String time;
    public String capacity;
    public String address;
    public String title;

    // Optional
    public String description;
    public String[] members;
    public String access;

    public CreatePartyRequest(String owner, String time, String capacity, String address, String title, String description, String[] members, String access) {
        this.owner = owner;
        this.time = time;
        this.capacity = capacity;
        this.address = address;
        this.title = title;
        this.description = description;
        this.members = members;
        this.access = access;
    }

    public CreatePartyRequest() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
