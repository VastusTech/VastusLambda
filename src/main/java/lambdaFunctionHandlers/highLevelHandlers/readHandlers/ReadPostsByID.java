package main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers;

import main.java.databaseObjects.Post;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;
import main.java.lambdaFunctionHandlers.responseObjects.PostResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadPostsByID {
    public static List<ObjectResponse> handle(String[] postIDs) throws Exception {
        List<ObjectResponse> postResponses = new ArrayList<>();
        for (String id : postIDs) {
            postResponses.add(new PostResponse(Post.readPost(id)));
        }
        return postResponses;
    }
}
