package main.java.databaseOperations.databaseActionBuilders;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

import main.java.databaseObjects.Streak;
import main.java.databaseOperations.CreateDatabaseAction;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.DeleteDatabaseAction;
import main.java.databaseOperations.UpdateDatabaseAction;
import main.java.databaseOperations.UpdateWithIDHandler;
import main.java.lambdaFunctionHandlers.requestObjects.CreateStreakRequest;

import static main.java.databaseOperations.UpdateDatabaseAction.UpdateAction.*;

public class StreakDatabaseActionBuilder {
    final static private String itemType = "Streak";

    private static PrimaryKey getPrimaryKey(String id) {
        return new PrimaryKey("item_type", itemType, "id", id);
    }

    public static DatabaseAction create(CreateStreakRequest createStreakRequest, boolean ifWithCreate) {
        // Handle the setting of the items
        Map<String, AttributeValue> item = Streak.getEmptyItem();
        item.put("owner", new AttributeValue(createStreakRequest.owner));
        item.put("about", new AttributeValue(createStreakRequest.about));
        item.put("streakType", new AttributeValue(createStreakRequest.streakType));
        if (createStreakRequest.updateType != null) { item.put("updateType", new AttributeValue(createStreakRequest.updateType)); }
        return new CreateDatabaseAction(itemType, item, ifWithCreate, new UpdateWithIDHandler() {
            @Override
            public void updateWithID(Map<String, AttributeValue> item, String id) throws Exception {
                return;
            }
        });
    }

    public static DatabaseAction updateLastUpdated(String id, String lastUpdated) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "lastUpdated", new AttributeValue(lastUpdated), false, PUT);
    }

    public static DatabaseAction updateAddN(String id) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "N", new AttributeValue().withN("1"), false, ADD);
    }

    public static DatabaseAction updateAddBestN(String id) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "bestN", new AttributeValue().withN("1"), false, ADD);
    }

    public static DatabaseAction updateAddCurrentN(String id) throws Exception {
        return new UpdateDatabaseAction(id, itemType, getPrimaryKey(id), "currentN", new AttributeValue().withN("1"), false, ADD);
    }

    public static DatabaseAction delete(String id) {
      return new DeleteDatabaseAction(id, itemType, getPrimaryKey(id));
    }
}
