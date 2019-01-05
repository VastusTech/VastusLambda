package main.java.Logic;

public enum ItemType {
    Client,
    Trainer,
    Gym,
    Workout,
    Review,
    Event,
    Challenge,
    Invite,
    Post,
    Group,
    Comment,
    Sponsor,
    Message;

    public static String getItemType(String id) throws Exception {
        String prefix = id.substring(0, Constants.numPrefix);
        ItemType[] itemTypes = ItemType.values();

        for (ItemType itemType : itemTypes) {
            String type = itemType.name();
            if (prefix.equals(type.substring(0, Constants.numPrefix).toUpperCase())) {
                return type;
            }
        }

        throw new Exception("Couldn't recognize the item type of the ID = " + id);
    }

    public static boolean ifItemType(String itemType) {
        try {
            ItemType.valueOf(itemType);
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
