package main.java.databaseObjects;

import com.amazonaws.services.dynamodbv2.document.Item;
import main.java.Logic.ItemType;

public interface DatabaseObjectBuilder {
    static DatabaseObject build(Item item) throws Exception {
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
            default:
                throw new Exception("The item type is improperly formed");
        }
    }
}
