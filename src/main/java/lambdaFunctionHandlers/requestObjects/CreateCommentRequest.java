package main.java.lambdaFunctionHandlers.requestObjects;

public class CreateCommentRequest {
    // Required
    public String by;
    public String to;
    public String comment;

    // Optional

    public CreateCommentRequest(String by, String to, String comment) {
        this.by = by;
        this.to = to;
        this.comment = comment;
    }

    public CreateCommentRequest() {}

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}