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
     * TODO
     *
     * @param channel
     * @param type
     */
    public Notification(String channel, String type) {
        this.channel = channel;
        this.type = type;
        this.payloadFields = new HashMap<>();
    }

    /**
     * TODO
     *
     * @param channel
     * @param type
     * @param payloadFields
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
     * TODO
     *
     * @param name
     * @param field
     */
    public void addPayloadField(String name, Map<String, Object> field) {
        payloadFields.put(name, field);
    }

    /**
     * TODO
     *
     * @param name
     * @param field
     */
    public void addPayloadField(String name, boolean field) {
        payloadFields.put(name, field);
    }

    /**
     * TODO
     *
     * @param name
     * @param field
     * @param value
     */
    public void addToPayloadField(String name, String field, Object value) {
        getMapField(name).put(field, value);
    }

    /**
     * TODO
     *
     * @param name
     * @param field
     * @param values
     */
    public void addToPayloadFieldStringSet(String name, String field, Set<String> values) {
        getSetFromField(name, field).addAll(values);
    }

    // Getters for the payload-level

    /**
     * TODO
     *
     * @param name
     * @return
     */
    private Object getPayloadField(String name) {
        return payloadFields.get(name);
    }

    /**
     * TODO
     *
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMapField(String name) {
        return (Map<String, Object>)getPayloadField(name);
    }

    /**
     * TODO
     *
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean getBooleanField(String name) {
        return (boolean)getPayloadField(name);
    }

    // Getters one level above the payload-level

    /**
     * TODO
     *
     * @param name
     * @param mapName
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMapFromField(String name, String mapName) {
        return (Map<String, Object>)getMapField(name).get(mapName);
    }

    /**
     * TODO
     *
     * @param name
     * @param stringName
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getStringFromField(String name, String stringName) {
        return (String)getMapField(name).get(stringName);
    }

    /**
     * TODO
     *
     * @param name
     * @param listName
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<Object> getSetFromField(String name, String listName) {
        return (Set<Object>)getMapField(name).get(listName);
    }

    /**
     * TODO
     *
     * @param name
     * @param booleanName
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean getBooleanFromField(String name, String booleanName) {
        return (boolean)getMapField(name).get(booleanName);
    }

    /**
     * TODO
     *
     * @param name
     * @return
     */
    public boolean payloadContainsKey(String name) {
        return this.payloadFields.containsKey(name);
    }

    /**
     * TODO
     *
     * @param name
     * @param fieldName
     * @return
     */
    public boolean nestedPayloadFieldContainsKey(String name, String fieldName) {
        return getMapField(name).containsKey(fieldName);
    }

    /**
     * TODO
     *
     * @return
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
