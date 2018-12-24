package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Comment;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateCommentRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentDatabaseActionBuilder {
    final static private String itemType = "Comment";

    public static DatabaseAction create(CreateCommentRequest createCommentRequest) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Comment.getEmptyItem();
        item.put("by", new AttributeValue(createCommentRequest.by));
        item.put("on", new AttributeValue(createCommentRequest.on));
        item.put("comment", new AttributeValue(createCommentRequest.comment));
        return new CreateDatabaseAction(item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction updateComment(String id, String comment) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "comment", new AttributeValue(comment), false, "PUT");
    }

    public static DatabaseAction updateAddComment(String id, String comment, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(id, itemType, "comments", null, true, "ADD");
        }
        else {
            return new UpdateDatabaseAction(id, itemType, "comments", new AttributeValue(comment), false, "ADD");
        }
    }

    public static DatabaseAction updateRemoveComment(String id, String comment) throws Exception {
        return new UpdateDatabaseAction(id, itemType, "comments", new AttributeValue(comment), false, "DELETE");
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
