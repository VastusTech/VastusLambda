package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Comment;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateCommentRequest;

import java.util.HashMap;
import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class CommentDatabaseActionBuilder {
    final static private String itemType = "Comment";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateCommentRequest createCommentRequest) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Comment.getEmptyItem();
        item.put("by", new AttributeValue(createCommentRequest.by));
        item.put("to", new AttributeValue(createCommentRequest.to));
        item.put("comment", new AttributeValue(createCommentRequest.comment));
        return new CreateDatabaseAction(itemType, item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction updateComment(String id, String comment) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "comment", new AttributeValue(comment), false, PUT);
    }

    public static DatabaseAction updateAddComment(String id, String comment, boolean ifWithCreate) throws Exception {
        if (ifWithCreate) {
            return new UpdateDatabaseAction(getPrimaryKey(id), "comments", null, true, ADD);
        }
        else {
            return new UpdateDatabaseAction(getPrimaryKey(id), "comments", new AttributeValue(comment), false, ADD);
        }
    }

    public static DatabaseAction updateRemoveComment(String id, String comment) throws Exception {
        return new UpdateDatabaseAction(getPrimaryKey(id), "comments", new AttributeValue(comment), false, DELETE);
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(itemType, getPrimaryKey(id));
    }
}