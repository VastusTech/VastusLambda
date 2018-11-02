package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Party;

public class PartyResponse extends ObjectResponse {
    public String item_type = "Party";
    public String owner;
    public String time;
    public String[] members;
    public String capacity;
    public String access;
    public String address;

    public String title;
    public String description;

    public PartyResponse(Party party) {
        super(party.id);
        this.owner = party.owner;
        this.time = party.time.toString();
        this.members = party.members.toArray(new String[]{});
        this.capacity = Integer.toString(party.capacity);
        this.access = party.access;
        this.address = party.address;
    }
}
