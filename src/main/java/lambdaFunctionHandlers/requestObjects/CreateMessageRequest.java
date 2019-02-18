package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateMessageRequest extends CreateObjectRequest {
    // Required
    public String board;
    public String from;
    public String message;
    public String name;

    // Optional
    public String type;
    public String profileImagePath; // TODO This isn't super efficient from the front-end perspective

    public CreateMessageRequest(String board, String from, String name, String profileImagePath, String message, String type) {
        this.board = board;
        this.from = from;
        this.name = name;
        this.profileImagePath = profileImagePath;
        this.message = message;
        this.type = type;
    }

    public CreateMessageRequest() {}

    @Override
    public boolean ifHasEmptyString() {
        return hasEmptyString(board, from, message, name, type, profileImagePath);
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
