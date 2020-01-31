package main.java.lambdaFunctionHandlers.requestObjects;

import main.java.databaseOperations.exceptions.ExceedsDatabaseLimitException;

/**
 * The POJO for the request if the Lambda caller wants to create a Submission in the database.
 */
public class CreateSubmissionRequest extends CreateObjectRequest {
    // Required
    public String by;
    public String description;
    public String about;

    // Optional
    public String[] picturePaths;
    public String[] videoPaths;

    public CreateSubmissionRequest(String by, String description, String about, String[] picturePaths,
                                   String[] videoPaths) {
        this.by = by;
        this.description = description;
        this.about = about;
        this.picturePaths = picturePaths;
        this.videoPaths = videoPaths;
    }

    public CreateSubmissionRequest() {}

    @Override
    public boolean ifHasEmptyString() throws ExceedsDatabaseLimitException {
        return hasEmptyString(by, description, about) ||
                arrayHasEmptyString(picturePaths, videoPaths);
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
