package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Party;

public class PartyResponse extends ObjectResponse {
    public String itemType = "Party";
    public String ownerID;
    public String time;
    public String[] memberIDs;
    public String capacity;
    public String access;
    public String address;

    public String title;
    public String description;

    public PartyResponse(Party party) {
        super(party.id);
        this.ownerID = party.ownerID;
        this.time = party.time.toString();
        this.memberIDs = party.memberIDs.toArray(new String[]{});
        this.capacity = Integer.toString(party.capacity);
        this.access = party.access;
        this.address = party.address;
    }
}
