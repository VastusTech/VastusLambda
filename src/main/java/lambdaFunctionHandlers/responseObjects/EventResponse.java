package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Event;

public class EventResponse extends ObjectResponse {
    public String item_type = "Event";
    public String owner;
    public String time;
    public String[] members;
    public String[] invitedMembers;
    public String capacity;
    public String access;
    public String address;
    public String ifCompleted;

    public String title;
    public String description;
    public String[] tags;

    public EventResponse(Event event) {
        super(event.id);
        this.owner = event.owner;
        this.time = event.time.toString();
        this.members = event.members.toArray(new String[]{});
        this.invitedMembers = event.invitedMembers.toArray(new String[]{});
        this.capacity = Integer.toString(event.capacity);
        this.access = event.access;
        this.address = event.address;
        this.title = event.title;
        this.description = event.description;
        this.ifCompleted = Boolean.toString(event.ifCompleted);
        this.tags = event.tags.toArray(new String[]{});
    }
}

