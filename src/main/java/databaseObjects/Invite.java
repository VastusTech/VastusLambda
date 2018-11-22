package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import main.java.databaseOperations.DynamoDBHandler;
import main.java.lambdaFunctionHandlers.responseObjects.InviteResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.HashMap;
import java.util.Map;

public class Invite extends DatabaseObject {
    public String from;
    public String to;
    public String inviteType;
    public String about;
    public String description;

    Invite(Item item) throws Exception {
        super(item);
        this.from = item.getString("from");
        this.to = item.getString("to");
        this.inviteType = item.getString("inviteType");
        this.about = item.getString("about");
        this.description = item.getString("description");
    }

    public static Map<String, AttributeValue> getEmptyItem() {
        Map<String, AttributeValue> item = DatabaseObject.getEmptyItem();
        item.put("item_type", new AttributeValue("Invite"));
        return item;
    }

    @Override
    public ObjectResponse getResponse() {
        return new InviteResponse(this);
    }

    // TODO Implement cache system here again?
    public static Invite readInvite(String id) throws Exception {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("item_type", new AttributeValue("Invite"));
        key.put("id", new AttributeValue(id));
        return DynamoDBHandler.getInstance().readItem(key);
    }
}

