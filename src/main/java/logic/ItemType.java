package main.java.logic;

/**
 * This enum represents the item types that are possible within our entire application. Also
 * includes logic to infer the item type from the ID.
 */
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
    Submission,
    Group,
    Comment,
    Sponsor,
    Message,
    Streak,
    Enterprise,;

    /**
     * Gets the item type from the given ID. Infers using the prefix at the beginning of the id.
     * TODO Use a static map and get a constant time retrieval!!!
     *
     * @param id The ID of the item in the database.
     * @return The string of the item type that corresponds to this ID value.
     * @throws Exception If the prefix of the ID was unrecognized. TODO Define exception?
     */
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

    /**
     * Checks to see if the item type is a valid item type.
     *
     * @param itemType The string of the alleged item type.
     * @return Whether or not it is a valid item type.
     */
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
