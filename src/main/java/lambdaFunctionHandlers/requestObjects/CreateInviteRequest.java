package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateInviteRequest {
    // Required
    public String from;
    public String to;
    public String inviteType;
    public String about;

    // Not required
    public String description;

    public CreateInviteRequest(String from, String to, String inviteType, String about, String description) {
        this.from = from;
        this.to = to;
        this.inviteType = inviteType;
        this.about = about;
        this.description = description;
    }

    public CreateInviteRequest() {}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getInviteType() {
        return inviteType;
    }

    public void setInviteType(String inviteType) {
        this.inviteType = inviteType;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}