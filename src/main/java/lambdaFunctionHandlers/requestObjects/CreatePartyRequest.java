package main.java.lambdaFunctionHandlers.requestObjects;

public class CreatePartyRequest {
    // Required
    public String ownerID;
    public String time;
    public String capacity;
    public String address;
    public String title;

    // Optional
    public String description;
    public String[] memberIDs;
    public String access;

    public CreatePartyRequest(String ownerID, String time, String capacity, String address, String title, String description, String[] memberIDs, String access) {
        this.ownerID = ownerID;
        this.time = time;
        this.capacity = capacity;
        this.address = address;
        this.title = title;
        this.description = description;
        this.memberIDs = memberIDs;
        this.access = access;
    }

    public CreatePartyRequest() {}

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
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

    public String[] getMemberIDs() {
        return memberIDs;
    }

    public void setMemberIDs(String[] memberIDs) {
        this.memberIDs = memberIDs;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
