package main.java.lambdaFunctionHandlers.requestObjects;

import main.java.databaseOperations.exceptions.ExceedsDatabaseLimitException;

/**
 * The POJO for the request if the Lambda caller wants to create a Group in the database.
 */
public class CreateGroupRequest extends CreateObjectRequest {
    // Required
    public String title;
    public String access;

    // Optional
    public String description;
    public String motto;
    public String groupImagePath;
    public String[] owners;
    public String[] members;
    public String[] tags;
    public String restriction;

    public CreateGroupRequest(String title, String description, String access, String motto, String groupImagePath,
                              String[] owners, String[] members, String[] tags, String restriction) {
        this.title = title;
        this.description = description;
        this.access = access;
        this.motto = motto;
        this.groupImagePath = groupImagePath;
        this.owners = owners;
        this.members = members;
        this.tags = tags;
        this.restriction = restriction;
    }

    public CreateGroupRequest() {}

    @Override
    public boolean ifHasEmptyString() throws ExceedsDatabaseLimitException {
        return hasEmptyString(title, description, access, motto, groupImagePath, restriction)
                || arrayHasEmptyString(owners, members, tags);
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

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getGroupImagePath() {
        return groupImagePath;
    }

    public void setGroupImagePath(String groupImagePath) {
        this.groupImagePath = groupImagePath;
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
