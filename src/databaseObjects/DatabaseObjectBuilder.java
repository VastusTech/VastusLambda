package databaseObjects;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public interface DatabaseObjectBuilder {
    static DatabaseObject build(Map<String, AttributeValue> item) {
        try {
            String itemType = item.get("item_type").getS();
            switch (itemType) {
                case "Client":
                    return new Client(item);
                case "Trainer":
                    return new Trainer(item);
                case "Gym":
                    return new Gym(item);
                case "Workout":
                    return new Workout(item);
                case "Review":
                    return new Review(item);
                default:
                    throw new Exception("The item type is improperly formed");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
