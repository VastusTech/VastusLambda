package main.java.notifications;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import main.java.logic.Constants;

/**
 * Compiles the notifications together so that it sends all relevant information to one channel at
 * a time. For instance, if two notifications were going to be sent to a single channel, this class
 * would be responsible for compiling them into a single message.
 *
 * TODO SHOULD WE CHECK TO SEE IF WE'RE BOTH REMOVING AND ADDING FROM AN ATTRIBUTE? I HAVE NO IDEA
 * TODO WHAT WE SHOULD DO IN THAT CASE?
 *
 * If we have both a add and a remove, do we add it or do we remove it?
 */
public class ObjectUpdateNotificationCompiler {
    private final static String type = "ObjectUpdate";
    private Map<String, Notification> notifications;

    /**
     * Constructor for the NotificationCompiler class.
     */
    public ObjectUpdateNotificationCompiler() {
        notifications = new HashMap<>();
    }

    /**
     * Gets a Collection of Notifications from the map.
     *
     * @return The final notifications compiled by the instance.
     */
    public Collection<Notification> getNotifications() {
        return notifications.values();
    }

    /**
     * Gets the Channel Name from the ID as specified by our API.
     *
     * @param id The ID of the object
     */
    private String getChannelNameForID(String id) {
        return id + "-Updates";
    }

    /**
     * Tells the compiler that this object will need the create object in order to do its job most
     * efficiently.
     *
     * @param id The id of the object to be notified of the creation of the object.
     */
    void setCreateObjectFlag(String id) {
        String updateType = "CREATE";
        String channel = getChannelNameForID(id);
        Notification notification = notifications.get(channel);
        if (notification == null) {
            addUpdateType(channel, updateType, new HashMap<>());
        }
        else {
            notification.addPayloadField(updateType, new HashMap<>());
        }
    }

    /**
     * This is called once the createObject is fully decided and it can be filled in in all the
     * object notifications that had set the create object flag in their payloads. This will also
     * replace all the notifications that set their object to send as "" in order to put the ID in.
     *
     * @param createObject The object to send through each notification.
     */
    void fillCreateObjectFields(String id, Map<String, Object> createObject) {
        for (Notification notification : getNotifications()) {
            Constants.debugLog("Notification before being filled = " + notification.toString() + "\n");
            if (notification.payloadContainsKey("SET")) {
                notification.replaceFromPayloadField("SET", "", id);
            }
            if (notification.payloadContainsKey("ADD")) {
                notification.replaceFromPayloadField("ADD", "", id);
            }
            if (notification.payloadContainsKey("REMOVE")) {
                notification.replaceFromPayloadField("REMOVE", "", id);
            }
            if (notification.payloadContainsKey("CREATE")) {
                notification.addPayloadField("CREATE", createObject);
            }
            Constants.debugLog("Notification after being filled = " + notification.toString() + "\n");

        }
    }


    /**
     * Adds a SET update expression to the notifications to an object channel. Tells the object to
     * set a certain field to a certain value.
     *
     * @param id The object ID to send the update SET to.
     * @param attributeName The name of the field in the object.
     * @param attributeValue The value to switch the field in the object to.
     */
    void addSetObjectFieldNotification(String id, String attributeName, String attributeValue) {
        setUpdateTypeField(getChannelNameForID(id), attributeName, attributeValue);
    }

    /**
     * Adds a SET update expression to the notifications to an object channel. Tells the object to
     * set a certain field to certain values. Specifically updates it to an array.
     *
     * @param id The object ID to send the update SET to.
     * @param attributeName The name of the field in the object.
     * @param attributeValues The values to set to that object's field.
     */
    void addSetObjectFieldArrayNotification(String id, String attributeName, Set<String> attributeValues) {
        setUpdateTypeField(getChannelNameForID(id), attributeName, attributeValues);
    }

    /**
     * Adds a ADD update expression to the notifications to an object channel. Tells the object to
     * add certain values to the field.
     *
     * @param id The object ID to send the update ADD to.
     * @param attributeName The name of the field in that object to update.
     * @param attributeValues The values to add to that object's field.
     */
    void addAddToObjectFieldNotification(String id, String attributeName, Set<String> attributeValues) {
        addToUpdateTypeFieldSet(getChannelNameForID(id), "ADD", attributeName, attributeValues);
    }

    /**
     * Adds a REMOVE update expression to the notifications to an object channel. Tells the object
     * to remove certain values from the field.
     *
     * @param id The object ID to send the update REMOVE from.
     * @param attributeName The name of the field in that object to update.
     * @param attributeValues The values to remove from that object's field.
     */
    void addRemoveFromObjectFieldNotification(String id, String attributeName, Set<String> attributeValues) {
        addToUpdateTypeFieldSet(getChannelNameForID(id), "REMOVE", attributeName, attributeValues);
    }


    /**
     * Adds a DELETE expression to the notifications to an object channel. Tells the object to be
     * deleted from the client.
     *
     * @param id The object ID to send the DELETE to.
     */
    void setDeleteObjectNotification(String id) {
        String channel = getChannelNameForID(id);
        Notification notification = notifications.get(channel);
        if (notification == null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("DELETE", true);
            notifications.put(channel, new Notification(channel, type, payload));
        }
        else {
            // TODO Check to see if there are any other fields and delete them?
            notification.addPayloadField("DELETE", true);
        }
    }

    /**
     * Adds a update type with a "JSON" to the payload and the notifications.
     *
     * @param channel The channel to send the update to.
     * @param updateType The type of the update to add to the payload.
     * @param updateJSON The "JSON" to add to the payload and the notification.
     */
    private void addUpdateType(String channel, String updateType, Map<String, Object> updateJSON) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(updateType, updateJSON);
        notifications.put(channel, new Notification(channel, type, payload));
    }

    /**
     * Adds a update type with an Object field to the payload and the notifications.
     *
     * @param channel The channel to send the update to.
     * @param updateType The type of the update to add to the payload.
     * @param fieldName The field name to initialize the payload and notification with.
     * @param value The value to initialize the payload and notification with.
     */
    private void addUpdateTypeWithField(String channel, String updateType, String fieldName, Object value) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(fieldName, value);
        addUpdateType(channel, updateType, updateMap);
    }

    /**
     * Adds a "SET" expression to the payload for an object. Potentially creates a new
     * notification. Otherwise interpolates the existing values together.
     *
     * @param channel The channel to send the notification update to.
     * @param fieldName The field name to update with.
     * @param value The value to update the field with.
     */
    private void setUpdateTypeField(String channel, String fieldName, Object value) {
        String updateType = "SET";
        Notification notification = notifications.get(channel);
        if (notification == null) {
            addUpdateTypeWithField(channel, updateType, fieldName, value);
        }
        else {
            // Check to see if this was already updated
            if (notification.payloadContainsKey(updateType)) {
                // Then we want to throw an exception because we're trying to overwrite a CREATE
                notification.addToPayloadField(updateType, fieldName, value);
            }
            else {
                // Otherwise, we add "ADD" to the notification
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put(fieldName, value);
                notification.addPayloadField(updateType, updateMap);
            }
        }
    }

    /**
     * Adds a "ADD" or "REMOVE" update expression to a payload for an object. Potentially creates a
     * new notification. Otherwise interpolates the values together.
     *
     * @param channel The channel to send the the notification update to.
     * @param updateType The type of update to use. ("ADD" or "REMOVE").
     * @param fieldName The name of the field to include in the update statement.
     * @param values The Set of String values to include with the update to the field.
     */
    private void addToUpdateTypeFieldSet(String channel, String updateType, String fieldName, Set<String> values) {
        Notification notification = notifications.get(channel);
        if (notification == null) {
            addUpdateTypeWithField(channel, updateType, fieldName, values);
        }
        else {
            // Check to see if this was already updated
            if (notification.payloadContainsKey(updateType)) {
                // If there already is an ADD
                // Check if that attribute is already in the notification
                if (notification.nestedPayloadFieldContainsKey(updateType, fieldName)) {
                    // If we are already updating that field, then we merge the two sets.
                    notification.addToPayloadFieldStringSet(updateType, fieldName, values);
                }
                else {
                    // Otherwise we just add the field to the JSON.
                    notification.addToPayloadField(updateType, fieldName, values);
                }
            }
            else {
                // Otherwise, we add the whole set from scratch.
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put(fieldName, values);
                notification.addPayloadField(updateType, updateMap);
            }
        }
    }
}
