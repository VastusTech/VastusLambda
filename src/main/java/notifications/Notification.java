package main.java.notifications;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 * Represents a single message to send through Ably to a client web application. Houses most of the
 * JSON logic.
 */
public class Notification {
    private String channel;
    private String type;
    private Map<String, Object> payloadFields;

    /**
     * Constructor for the Notification object that creates an empty notification to a channel and
     * of type.
     *
     * @param channel The channel name to send the notification to.
     * @param type The type (event name) to send the notification with.
     */
    public Notification(String channel, String type) {
        this.channel = channel;
        this.type = type;
        this.payloadFields = new HashMap<>();
    }

    /**
     * Constructor for the Notification object that initializes the notification with payload
     * fields to send.
     *
     * @param channel The channel name to send the notification to.
     * @param type The type (event name) to send the notification with.
     * @param payloadFields The map of payload fields to send through the notification.
     */
    public Notification(String channel, String type, Map<String, Object> payloadFields) {
        this.channel = channel;
        this.type = type;
        this.payloadFields = payloadFields;
    }
//
//    void addPayloadField(String name, Object field) {
//        payloadFields.put(name, field);
//    }
//
//    void addPayloadField(String name, String field) {
//        payloadFields.put(name, field);
//    }

    /**
     * Replaces all instances of a string occurrence in a field with a replacement.
     * IMPORTANT: Make sure the search object and the replace object are the same type.
     *
     * @param name The name of the field to replace all the occurrences from.
     * @param searchObject The Object to find.
     * @param replaceObject The Object to replace it with.
     */
    public void replaceFromPayloadField(String name, Object searchObject, Object replaceObject) {
        if (!searchObject.getClass().equals(replaceObject.getClass())) {
            // TODO Error with calling this
            System.err.println("WHAT u Doin'?");
            return;
        }
        Map<String, Object> map = getMapField(name);
        // Go through all the values in the map field
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String fieldName = entry.getKey();
            Object field = entry.getValue();
            // Check the type of it, either loop through the set or check the string value.
            if (field.getClass().equals(searchObject.getClass())) {
                if (field.equals(searchObject)) {
                    map.put(fieldName, replaceObject);
                }
            }
            else if (field instanceof Set) {
                // Check the type of the set only once
                boolean ifFirst = true;
                Set set = (Set)field;
                // This also acts as a check as a type checker?
                if (set.contains(searchObject)) {
                    set.remove(searchObject);
                    set.add(replaceObject);
                }
            }
            else {
                // Not implemented!
            }
        }
    }

    /**
     * Adds a "JSON" field to the existing payload.
     *
     * @param name The name of the field to add.
     * @param field The "JSON" object to add as the value.
     */
    void addPayloadField(String name, Map<String, Object> field) {
        payloadFields.put(name, field);
    }

    /**
     * Adds a boolean field to the existing payload.
     *
     * @param name The name of the field to add.
     * @param field The boolean to add as the value.
     */
    void addPayloadField(String name, boolean field) {
        payloadFields.put(name, field);
    }

    /**
     * Adds to a 1 level deep "JSON" object. Adds to the field of a certain field in the payload.
     *
     * @param name The name of the field containing the "JSON" object.
     * @param field The field name to call the added field.
     * @param value The value to add to the field.
     */
    void addToPayloadField(String name, String field, Object value) {
        getMapField(name).put(field, value);
    }

    /**
     * Add a set of String values to an existing field pointing to a set of Strings.
     *
     * @param name The name of the "JSON" object, holding the field with the Set of Strings.
     * @param field The field name in the "JSON" object, holding the existing Set of Strings
     * @param values The Set of String values to append to the existing Set.
     */
    void addToPayloadFieldStringSet(String name, String field, Set<String> values) {
        getSetFromField(name, field).addAll(values);
    }

    // Getters for the payload-level

    /**
     * Gets a top level payload field.
     *
     * @param name The name of the field to get.
     * @return The Object of the payload field.
     */
    private Object getPayloadField(String name) {
        return payloadFields.get(name);
    }

    /**
     * Gets a payload field as a "JSON" object.
     *
     * @param name The name of the "JSON" field.
     * @return The Map of Strings to Objects, representing a "JSON" object.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getMapField(String name) {
        return (Map<String, Object>)getPayloadField(name);
    }

    /**
     * Gets a top-level boolean field.
     *
     * @param name The name of the boolean field.
     * @return The boolean value in the field.
     */
    @SuppressWarnings("unchecked")
    private boolean getBooleanField(String name) {
        return (boolean)getPayloadField(name);
    }

    // Getters one level above the payload-level
    /**
     * Gets a "JSON" object from a field within a field.
     *
     * @param name The name of the top-level field which has the "JSON" field.
     * @param mapName The name of the field pointing to the "JSON" object.
     * @return The "JSON" String to Object Map.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getMapFromField(String name, String mapName) {
        return (Map<String, Object>)getMapField(name).get(mapName);
    }

    /**
     * Gets a "JSON" object from a field within a field.
     *
     * @param name The name of the top-level field which has the String field.
     * @param stringName The name of the field pointing to the String.
     * @return The String in the field.
     */
    @SuppressWarnings("unchecked")
    private String getStringFromField(String name, String stringName) {
        return (String)getMapField(name).get(stringName);
    }

    /**
     * Gets a Object Set object from a field within a field.
     *
     * @param name The name of the top-level field which has the Set field.
     * @param listName The name of the field pointing to the Object Set.
     * @return The Object Set in the field.
     */
    @SuppressWarnings("unchecked")
    private Set<Object> getSetFromField(String name, String listName) {
        return (Set<Object>)getMapField(name).get(listName);
    }

    /**
     * Gets a boolean value from a field within a field.
     *
     * @param name The name of the top-level field which has the boolean field.
     * @param booleanName The name of the field pointing to the boolean value.
     * @return The boolean value in the field.
     */
    @SuppressWarnings("unchecked")
    private boolean getBooleanFromField(String name, String booleanName) {
        return (boolean)getMapField(name).get(booleanName);
    }

    /**
     * Figures out whether the payload has a field with the given name.
     *
     * @param name The name of field to inquire about.
     * @return Whether the payload has a field with the given name.
     */
    boolean payloadContainsKey(String name) {
        return this.payloadFields.containsKey(name);
    }

    /**
     * Figures out whether the "JSON" object within a field has a field with a given name.
     *
     * @param name The name of the field containing the "JSON" object.
     * @param fieldName The name of the field in the "JSON" object to inquire about.
     * @return Whether the "JSON" object within a field has a field with the given name.
     */
    boolean nestedPayloadFieldContainsKey(String name, String fieldName) {
        return getMapField(name).containsKey(fieldName);
    }

    /**
     * Creates the correctly formatted JSON object for a message to send through Ably.
     *
     * @return The {@link JsonObjectBuilder} object representing the message object.
     */
    public JsonObjectBuilder createMessageJSON() {
        return Json.createObjectBuilder()
                .add("channel", channel)
                .add("type", type)
                .add("payload", Json.createObjectBuilder(payloadFields));
    }

    @Override
    public String toString() {
        return createMessageJSON().build().toString();
    }
}
