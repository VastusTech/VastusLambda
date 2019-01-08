package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseObjects.Invite;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.databaseOperations.UpdateWithIDHandler;
import main.java.lambdaFunctionHandlers.requestObjects.CreateInviteRequest;

import java.util.HashMap;
import java.util.Map;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class InviteDatabaseActionBuilder {
    final static private String itemType = "Invite";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateInviteRequest createInviteRequest) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Invite.getEmptyItem();
        item.put("from", new AttributeValue(createInviteRequest.from));
        item.put("to", new AttributeValue(createInviteRequest.to));
        item.put("inviteType", new AttributeValue(createInviteRequest.inviteType));
        item.put("about", new AttributeValue(createInviteRequest.about));
        if (createInviteRequest.description != null) { item.put("description", new AttributeValue(createInviteRequest
                .description)); }
        return new CreateDatabaseAction(itemType, item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction delete(String id) {
        return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
