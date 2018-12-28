package main.java.databaseOperations.databaseActionBuilders;

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

    public static DatabaseAction create(CreateInviteRequest createInviteRequest) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Invite.getEmptyItem();
        item.put("from", new AttributeValue(createInviteRequest.from));
        item.put("to", new AttributeValue(createInviteRequest.to));
        item.put("inviteType", new AttributeValue(createInviteRequest.inviteType));
        item.put("about", new AttributeValue(createInviteRequest.about));
        if (createInviteRequest.description != null) { item.put("description", new AttributeValue(createInviteRequest
                .description)); }
        return new CreateDatabaseAction(item, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue(itemType));
        key.put("id", new AttributeValue(id));
        return new DeleteDatabaseAction(key);
    }
}
