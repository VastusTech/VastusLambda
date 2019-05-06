package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;

import main.java.databaseOperations.exceptions.ItemTypeNotRecognizedException;
import main.java.logic.Constants;
import main.java.logic.ItemType;

/**
 * Factory Design Pattern in order to make retrieving from the database a lot easier, switching the
 * item_type. Independent from which Table it's taken from, so you can put in any valid Item object
 * and it will build it according to its item type.
 */
public interface DatabaseItemFactory {
    /**
     * Builds a DatabaseItem based on the "item_type" field in the item object.
     *
     * @param item The {@link Item} from the AWS module that represents a row in the database.
     * @return The fully built {@link DatabaseItem}.
     * @throws Exception If the item type of the item is unrecognized.
     */
    static DatabaseItem build(Item item) throws Exception {
        String itemType = (String)item.get("item_type");
        if (itemType == null) {
            throw new Exception("Item does not have the item_type attribute!!!");
        }
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
                throw new ItemTypeNotRecognizedException("Item type = " + itemType + " not implemented in DatabaseItemBuilder");
        }
    }
}
