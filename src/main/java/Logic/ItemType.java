package main.java.Logic;

public enum ItemType {
    Client,
    Trainer,
    Gym,
    Workout,
    Review,
    Event,
    Invite,;

    public static String getItemType(String id) {
        String prefix = id.substring(0, Constants.numPrefix);
        ItemType[] itemTypes = ItemType.values();

        for (ItemType itemType : itemTypes) {
            String type = itemType.name();
            if (prefix.equals(type.substring(0, Constants.numPrefix).toUpperCase())) {
                return type;
            }
        }

        return null;
    }
}
