package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Invite;

public class InviteResponse extends ObjectResponse {
    public String item_type = "Invite";
    public String from;
    public String to;
    public String inviteType;
    public String about;
    public String description;

    public InviteResponse(Invite invite) {
        super(invite.id);
        this.from = invite.from;
        this.to = invite.to;
        this.inviteType = invite.inviteType;
        this.about = invite.about;
        this.description = invite.description;
    }
}
