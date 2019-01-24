package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateMessageRequest {
    // Required
    public String board;
    public String from;
    public String message;
    public String name;

    // Optional
    public String type;
    public String profileImagePath;

    public CreateMessageRequest(String board, String from, String name, String profileImagePath, String message, String type) {
        this.board = board;
        this.from = from;
        this.name = name;
        this.profileImagePath = profileImagePath;
        this.message = message;
        this.type = type;
    }

    public CreateMessageRequest() {}

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
