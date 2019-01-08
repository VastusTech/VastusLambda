package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Message;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.databaseOperations.UpdateWithIDHandler;
import main.java.lambdaFunctionHandlers.requestObjects.CreateMessageRequest;

import java.util.HashMap;
import java.util.Map;

public class MessageDatabaseActionBuilder {
    private static final String itemType = "Message";

    private static PrimaryKey getPrimaryKey(String board, String id) {
        return new PrimaryKey("board", board, "id", id);
    }

    public static DatabaseAction create(CreateMessageRequest createMessageRequest) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Message.getEmptyItem();
        item.put("board", new AttributeValue(createMessageRequest.board));
        item.put("from", new AttributeValue(createMessageRequest.from));
        item.put("message", new AttributeValue(createMessageRequest.message));
        if (createMessageRequest.type != null) { item.put("type", new AttributeValue(createMessageRequest.type)); }
        return new CreateDatabaseAction(itemType, item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction delete(String board, String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(board, id));
    }
}
