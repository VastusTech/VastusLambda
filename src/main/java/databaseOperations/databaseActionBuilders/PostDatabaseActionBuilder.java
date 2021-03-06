package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Post;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreatePostRequest;

import java.util.*;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

/**
 * The Database Action Builder for the {@link Post} object, getting the {@link DatabaseAction} objects
 * that dictate the individual actions to do in the database for Posts.
 */
public class PostDatabaseActionBuilder {
    final static private String itemType = "Post";

    /**
     * Gets the {@link PrimaryKey} object to identify the object in the database.
     *
     * @param id The ID of the object to reference.
     * @return The {@link PrimaryKey} object to identify the database item with.
     */
    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreatePostRequest createPostRequest, Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Post.getEmptyItem();
        item.put("by", new AttributeValue(createPostRequest.by));
        item.put("access", new AttributeValue(createPostRequest.access));
        if (createPostRequest.description != null) { item.put("description", new AttributeValue(createPostRequest.description)); }
        if (createPostRequest.postType != null) { item.put("postType", new AttributeValue(createPostRequest.postType)); }
        if (createPostRequest.about != null) { item.put("about", new AttributeValue(createPostRequest.about)); }
        if (createPostRequest.picturePaths != null) { item.put("picturePaths", new AttributeValue(Arrays.asList
                (createPostRequest.picturePaths))); }
        if (createPostRequest.videoPaths != null) { item.put("videoPaths", new AttributeValue(Arrays.asList
                (createPostRequest.videoPaths))); }
        if (createPostRequest.group != null) { item.put("group", new AttributeValue(createPostRequest.group)); }
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
            (Map<String, AttributeValue> createdItem, String id) -> {
                if (createPostRequest.picturePaths != null && createPostRequest.picturePaths.length > 0) {
                    List<String> picturePaths = new ArrayList<>();
                    for (String picturePath : createPostRequest.picturePaths) {
                        picturePaths.add(id + "/" + picturePath);
                    }
                    createdItem.put("picturePaths", new AttributeValue(picturePaths));
                }
                if (createPostRequest.videoPaths != null && createPostRequest.videoPaths.length > 0) {
                    List<String> videoPaths = new ArrayList<>();
                    for (String videoPath : createPostRequest.videoPaths) {
                        videoPaths.add(id + "/" + videoPath);
                    }
                    createdItem.put("videoPaths", new AttributeValue(videoPaths));
                }
            }
        );
    }

    public static DatabaseAction updateAccess(String id, String access) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "access", new AttributeValue(access), false, PUT);
    }

    public static DatabaseAction updateDescription(String id, String description) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "description", new AttributeValue(description), false, PUT);
    }

    public static DatabaseAction updateAddPicturePath(String id, String picturePath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "picturePaths", new AttributeValue(picturePath), false, ADD);
    }

    public static DatabaseAction updateRemovePicturePath(String id, String picturePath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "picturePaths", new AttributeValue(picturePath), false, DELETE);
    }

    public static DatabaseAction updateAddVideoPath(String id, String videoPath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "videoPaths", new AttributeValue(videoPath), false, ADD);
    }

    public static DatabaseAction updateRemoveVideoPath(String id, String videoPath) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "videoPaths", new AttributeValue(videoPath), false, DELETE);
    }

    public static DatabaseAction updateAddLike(String id, String like) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "likes", new AttributeValue(like), false, ADD);
    }

    public static DatabaseAction updateRemoveLike(String id, String like) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "likes", new AttributeValue(like), false, DELETE);
    }

    public static DatabaseAction updateAddComment(String id, String comment, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "comments", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "comments", new AttributeValue(comment), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveComment(String id, String comment) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "comments", new AttributeValue(comment), false, DELETE);
    }

    public static DatabaseAction updateGroup(String id, String group) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "group", new AttributeValue(group), false, PUT);
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
