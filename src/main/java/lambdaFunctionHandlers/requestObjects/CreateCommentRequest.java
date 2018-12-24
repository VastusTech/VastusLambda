package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateCommentRequest {
    // Required
    public String by;
    public String on;
    public String comment;

    // Optional

    public CreateCommentRequest(String by, String on, String comment) {
        this.by = by;
        this.on = on;
        this.comment = comment;
    }

    public CreateCommentRequest() {}

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
