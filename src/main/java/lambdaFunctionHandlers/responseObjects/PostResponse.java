package main.java.lambdaFunctionHandlers.responseObjects;

import main.java.databaseObjects.Post;

public class PostResponse extends ObjectResponse {
    public String item_type = "Post";
    public String by;
    public String description;
    public String about;
    public String access;
    public String postType;
    public String[] picturePaths;
    public String[] videoPaths;

    public PostResponse(Post post) {
        super(post.id);
        this.by = post.by;
        this.description = post.description;
        this.about = post.about;
        this.access = post.access;
        this.postType = post.postType;
        this.picturePaths = post.picturePaths.toArray(new String[]{});
        this.videoPaths = post.videoPaths.toArray(new String[]{});
    }
}
