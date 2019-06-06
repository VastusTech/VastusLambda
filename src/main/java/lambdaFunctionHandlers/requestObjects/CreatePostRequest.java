package main.java.lambdaFunctionHandlers.requestObjects;

import main.java.databaseOperations.exceptions.ExceedsDatabaseLimitException;

/**
 * The POJO for the request if the Lambda caller wants to create a Post in the database.
 */
public class CreatePostRequest extends CreateObjectRequest {
    // Required
    public String by;

    // Optional
    public String access;
    public String description;
    public String postType;
    public String about;
    public String[] picturePaths;
    public String[] videoPaths;
    public String group;

    public CreatePostRequest(String by, String description, String about, String access, String postType, String[]
            picturePaths, String[] videoPaths, String group) {
        this.by = by;
        this.description = description;
        this.about = about;
        this.access = access;
        this.postType = postType;
        this.picturePaths = picturePaths;
        this.videoPaths = videoPaths;
        this.group = group;
    }

    public CreatePostRequest() {}

    @Override
    public boolean ifHasEmptyString() throws ExceedsDatabaseLimitException {
        return hasEmptyString(by, description, about, access, postType, group)
                || arrayHasEmptyString(picturePaths, videoPaths);
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String[] getPicturePaths() {
        return picturePaths;
    }

    public void setPicturePaths(String[] picturePaths) {
        this.picturePaths = picturePaths;
    }

    public String[] getVideoPaths() {
        return videoPaths;
    }

    public void setVideoPaths(String[] videoPaths) {
        this.videoPaths = videoPaths;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
