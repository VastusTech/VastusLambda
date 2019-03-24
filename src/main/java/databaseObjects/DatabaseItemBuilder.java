package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import main.java.Logic.Constants;
import main.java.Logic.ItemType;

public interface DatabaseItemBuilder {
    static DatabaseItem build(Item item) throws Exception {
        String itemType = (String)item.get("item_type");
        Constants.debugLog("Building database item with itemType = " + itemType + ", item = " + item.toJSONPretty());
        switch (ItemType.valueOf(itemType)) {
            case Client:
                return new Client(item);
            case Trainer:
                return new Trainer(item);
            case Gym:
                return new Gym(item);
            case Workout:
                return new Workout(item);
            case Review:
                return new Review(item);
            case Event:
                return new Event(item);
            case Challenge:
                return new Challenge(item);
            case Invite:
                return new Invite(item);
            case Post:
                return new Post(item);
            case Group:
                return new Group(item);
            case Comment:
                return new Challenge(item);
            case Sponsor:
                return new Sponsor(item);
            case Message:
                return new Message(item);
            case Streak:
                return new Streak(item);
            case Enterprise:
                return new Enterprise(item);
            default:
                throw new Exception("Item type = " + itemType + " not implemented in DatabaseItemBuilder");
        }
    }
}
