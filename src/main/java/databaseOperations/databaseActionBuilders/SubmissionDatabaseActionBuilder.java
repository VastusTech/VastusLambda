package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import main.java.databaseObjects.Submission;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.databaseOperations.UpdateDatabaseAction;
import main.java.lambdaFunctionHandlers.requestObjects.CreateSubmissionRequest;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class SubmissionDatabaseActionBuilder {
    final static private String itemType = "Submission";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateSubmissionRequest createSubmissionRequest, Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Submission.getEmptyItem();
        item.put("by", new AttributeValue(createSubmissionRequest.by));
        item.put("description", new AttributeValue(createSubmissionRequest.description));
        item.put("about", new AttributeValue(createSubmissionRequest.about));
        if (createSubmissionRequest.picturePaths != null) { item.put("picturePaths", new AttributeValue(Arrays.asList
                (createSubmissionRequest.picturePaths))); }
        if (createSubmissionRequest.videoPaths != null) { item.put("videoPaths", new AttributeValue(Arrays.asList
                (createSubmissionRequest.videoPaths))); }
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
                (Map<String, AttributeValue> createdItem, String id) -> {
                    if (createSubmissionRequest.picturePaths != null && createSubmissionRequest.picturePaths.length > 0) {
                        List<String> picturePaths = new ArrayList<>();
                        for (String picturePath : createSubmissionRequest.picturePaths) {
                            picturePaths.add(id + "/" + picturePath);
                        }
                        createdItem.put("picturePaths", new AttributeValue(picturePaths));
                    }
                    if (createSubmissionRequest.videoPaths != null && createSubmissionRequest.videoPaths.length > 0) {
                        List<String> videoPaths = new ArrayList<>();
                        for (String videoPath : createSubmissionRequest.videoPaths) {
                            videoPaths.add(id + "/" + videoPath);
                        }
                        createdItem.put("videoPaths", new AttributeValue(videoPaths));
                    }
                }
        );
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

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
