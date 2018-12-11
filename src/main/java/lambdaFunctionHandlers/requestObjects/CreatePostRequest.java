package main.java.lambdaFunctionHandlers.requestObjects;

public class CreatePostRequest {
    public String by;
    public String description;
    public String access;

    public String postType;
    public String about;
    public String[] picturePaths;
    public String[] videoPaths;

    public CreatePostRequest(String by, String description, String about, String access, String postType, String[] picturePaths, String[] videoPaths) {
        this.by = by;
        this.description = description;
        this.about = about;
        this.access = access;
        this.postType = postType;
        this.picturePaths = picturePaths;
        this.videoPaths = videoPaths;
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
}
