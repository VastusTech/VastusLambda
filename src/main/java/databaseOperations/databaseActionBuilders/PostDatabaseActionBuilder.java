package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.databaseOperations.UpdateDatabaseAction;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PostDatabaseActionBuilder {
    final static private String itemType = "Post";

    public static DatabaseAction create(CreatePostRequest createPostRequest) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Post.getEmptyItem();
        item.put("by", new AttributeValue(createPostRequest.by));
        item.put("description", new AttributeValue(createPostRequest.description));
        item.put("access", new AttributeValue(createPostRequest.access));
        item.put("postType", new AttributeValue(createPostRequest.postType));
        if (createPostRequest.about != null) { item.put("about", new AttributeValue(createPostRequest.about)); }
        if (createPostRequest.picturePaths != null) { item.put("picturePaths", new AttributeValue(Arrays.asList
                (createPostRequest.picturePaths))); }
        if (createPostRequest.videoPaths != null) { item.put("videoPaths", new AttributeValue(Arrays.asList
                (createPostRequest.videoPaths))); }
        return new CreateDatabaseAction(item);
    }

    public static DatabaseAction updateAccess(String id, String access) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "access", new AttributeValue(access), false, "PUT");
    }

    public static DatabaseAction updateDescription(String id, String description) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "description", new AttributeValue(description), false, "PUT");
    }

    public static DatabaseAction updateAddPicturePath(String id, String picturePath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "picturePaths", new AttributeValue(picturePath), false, "ADD");
    }

    public static DatabaseAction updateRemovePicturePath(String id, String picturePath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "picturePaths", new AttributeValue(picturePath), false, "DELETE");
    }

    public static DatabaseAction updateAddVideoPath(String id, String videoPath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "videoPaths", new AttributeValue(videoPath), false, "ADD");
    }

    public static DatabaseAction updateRemoveVideoPath(String id, String videoPath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "videoPaths", new AttributeValue(videoPath), false, "DELETE");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
