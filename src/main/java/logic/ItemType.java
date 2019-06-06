package main.java.logic;

import java.util.HashMap;
import java.util.Map;

import main.java.databaseOperations.exceptions.BadIDException;

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

    private static Map<String, String> prefixes = new HashMap<String, String>(){{
        for (ItemType itemType : ItemType.values()) {
            put(itemType.name().substring(0, Constants.numPrefix).toUpperCase(), itemType.name());
        }
    }};

    /**
     * Gets the item type from the given ID. Infers using the prefix at the beginning of the id.
     *
     * @param id The ID of the item in the database.
     * @return The string of the item type that corresponds to this ID value.
     * @throws BadIDException If the prefix of the ID was unrecognized or the ID is malformed.
     */
    public static String getItemType(String id) throws BadIDException {
        if (id == null || id.length() < 2) {
            throw new BadIDException("ID null or not large enough to get item type for. ID = " + id);
        }
        String prefix = id.substring(0, Constants.numPrefix);
        String itemType = prefixes.get(prefix);
        if (itemType == null) {
            throw new BadIDException("Couldn't recognize the item type of the ID = " + id);
        }
        return itemType;
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
        catch (NullPointerException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
