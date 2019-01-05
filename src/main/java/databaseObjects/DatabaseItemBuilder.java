package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import main.java.Logic.ItemType;

public interface DatabaseItemBuilder {
    static DatabaseItem build(Item item) throws Exception {
        String itemType = (String)item.get("item_type");
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
            default:
                throw new Exception("Item type = " + itemType + " not implemented in DatabaseItemBuilder");
        }
    }
}
