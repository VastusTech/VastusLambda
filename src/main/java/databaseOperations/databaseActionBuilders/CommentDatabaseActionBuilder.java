package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Comment;
import main.java.databaseOperations.*;
import main.java.lambdaFunctionHandlers.requestObjects.CreateCommentRequest;

import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class CommentDatabaseActionBuilder {
    final static private String itemType = "Comment";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateCommentRequest createCommentRequest, Map<String, String> passoverIdentifiers) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Comment.getEmptyItem();
        item.put("by", new AttributeValue(createCommentRequest.by));
        item.put("to", new AttributeValue(createCommentRequest.to));
        item.put("comment", new AttributeValue(createCommentRequest.comment));
        return new CreateDatabaseAction(itemType, item, passoverIdentifiers,
            (Map<String, AttributeValue> createdItem, String id) -> {
                return;
            }
        );
    }

    public static DatabaseAction updateComment(String id, String comment) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "comment", new AttributeValue(comment), false, PUT);
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
