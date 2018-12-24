package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateGroupRequest {
    // Required
    public String title;
    public String description;

    // Optional
    public String access;
    public String[] owners;
    public String[] members;
    public String[] tags;
    public String restriction;

    public CreateGroupRequest(String title, String description, String access, String[] owners, String[] members, String[] tags, String restriction) {
        this.title = title;
        this.description = description;
        this.access = access;
        this.owners = owners;
        this.members = members;
        this.tags = tags;
        this.restriction = restriction;
    }

    public CreateGroupRequest() {}

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

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String[] getOwners() {
        return owners;
    }

    public void setOwners(String[] owners) {
        this.owners = owners;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }
}
