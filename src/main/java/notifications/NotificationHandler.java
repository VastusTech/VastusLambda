package main.java.notifications;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

import main.java.Logic.Constants;

/**
 * Stores all the Notifications to send after the transaction(s), handles the conversion to a
 * send-able format (String), and the sending of the notifications through a Lambda invocation.
 */
public class NotificationHandler {
    private final static String messageEventType = "Message";
    private ObjectUpdateNotificationCompiler objectUpdateNotificationCompiler;
    private List<Notification> messageNotifications;

    /**
     * The default constructor, simply initializes the HashMap to being empty.
     */
    public NotificationHandler() {
        messageNotifications = new ArrayList<>();
        objectUpdateNotificationCompiler = new ObjectUpdateNotificationCompiler();
    }

    /**
     * Adds a message notification to the total notifications to send.
     *
     * @param board The board to send the message notifications to.
     * @param payload The json payload to be sent through (for this, should be the message object).
     */
    public void addMessageNotification(String board, Map<String, Object> payload) {
        messageNotifications.add(new Notification(board + "-Board", messageEventType, payload));
    }

    /**
     * Sets the create flag for an object notification to add the newly created object to it.
     *
     * @param id The id of the object to add the creation to.
     */
    public void setCreateFlag(String id) {
        objectUpdateNotificationCompiler.setCreateObjectFlag(id);
    }

    /**
     * Fills the created object into all the objects that had set the create flag.
     *
     * @param id The newly created object ID.
     * @param createObject The newly created object to fill into CREATE statements.
     */
    public void fillCreateObject(String id, Map<String, Object> createObject) {
        objectUpdateNotificationCompiler.fillCreateObjectFields(id, createObject);
    }

    /**
     * Adds a set update notification to an object. This tells the object to change a field to a
     * certain String value.
     *
     * @param id The id of the object to update.
     * @param attributeName The attribute name in the object to alter.
     * @param attributeValue The value to set to the attribute.
     * @param ifWithCreate If the attributeValue to put in is the ID of the object to be created.
     */
    public void addSetNotification(String id, String attributeName, String attributeValue, boolean ifWithCreate) {
        if (ifWithCreate) { attributeValue = ""; } // Make sure that the attributeValue is empty
        objectUpdateNotificationCompiler.addSetObjectFieldNotification(id, attributeName, attributeValue);
    }

    /**
     * Adds a set update notification to an object. This tells the object to change a field to a
     * certain set of Strings.
     *
     * @param id The id of the object to update.
     * @param attributeName The attribute name in the object to alter.
     * @param attributeValues The value to set in the attribute.
     */
    public void addSetNotification(String id, String attributeName, Set<String> attributeValues) {
        objectUpdateNotificationCompiler.addSetObjectFieldArrayNotification(id, attributeName, attributeValues);
    }

    /**
     * Adds an add update notification to an object. This tells the object to add to a set in a
     * field with a single String value.
     *
     * @param id The id of the object to update.
     * @param attributeName The attribute name in the object to alter.
     * @param attributeValue The value to add to the attribute.
     * @param ifWithCreate If the attributeValue to add is the ID of the object to be created.
     */
    public void addAddNotification(String id, String attributeName, String attributeValue, boolean ifWithCreate) {
        if (ifWithCreate) { attributeValue = ""; }
        Set<String> set = new HashSet<>();
        set.add(attributeValue);
        objectUpdateNotificationCompiler.addAddToObjectFieldNotification(id, attributeName, set);
    }

    /**
     * Adds an add update notification to an object. This tells the object to add to a set in a
     * field with multiple String values.
     *
     * @param id The id of the object to update.
     * @param attributeName The attribute name in the object to alter.
     * @param attributeValues The value to add to the attribute.
     */
    public void addAddNotification(String id, String attributeName, Set<String> attributeValues) {
        objectUpdateNotificationCompiler.addAddToObjectFieldNotification(id, attributeName, attributeValues);
    }

    /**
     * Adds a remove update notification to an object. This tells the object to remove from a set in
     * a field with a single String value.
     *
     * @param id The id of the object to update.
     * @param attributeName The attribute name in the object to alter.
     * @param attributeValue The value to remove from the attribute.
     * @param ifWithCreate If the attributeValue to remove is the ID of the object to be created.
     */
    public void addRemoveNotification(String id, String attributeName, String attributeValue, boolean ifWithCreate) {
        if (ifWithCreate) { attributeValue = ""; }
        Set<String> set = new HashSet<>();
        set.add(attributeValue);
        objectUpdateNotificationCompiler.addRemoveFromObjectFieldNotification(id, attributeName, set);
    }

    /**
     * Adds a remove update notification to an object. This tells the object to remove from a set in
     * a field with multiple String values.
     *
     * @param id The id of the object to update.
     * @param attributeName The attribute name in the object to alter.
     * @param attributeValues The value to add to the attribute.
     */
    public void addRemoveNotification(String id, String attributeName, Set<String> attributeValues) {
        objectUpdateNotificationCompiler.addRemoveFromObjectFieldNotification(id, attributeName, attributeValues);
    }

    /**
     * Adds a delete notification to an object. This tells the object that it was deleted and it
     * should be removed from the app.
     *
     * @param id The id of the object to delete.
     */
    public void setDeleteNotification(String id) {
        objectUpdateNotificationCompiler.setDeleteObjectNotification(id);
    }

    /**
     * Getter for the object update compiler so I don't have to just write all the same functions in
     * this class, we can just use the getter.
     *
     * @return The object update compiler for this instance.
     */
    public ObjectUpdateNotificationCompiler getUpdateCompiler() {
        return objectUpdateNotificationCompiler;
    }

    /**
     * Sends the messages to the Ably Lambda Function with the notifications payload.
     */
    public void sendMessages() {
        String payload = getNotificationsPayload();
        Constants.debugLog("Sending payload to ABLY. Payload = \n" + payload);
        LambdaInvokeHandler.getInstance().invokeLambda(Constants.ablyFunctionName, payload);
    }

    /**
     * Gets the String representation of the payload to send through Lambda invocation.
     *
     * @return The payload to send through Lambda.
     */
    private String getNotificationsPayload() {
        JsonArrayBuilder json = Json.createArrayBuilder();
        List<Notification> notifications = new ArrayList<>(messageNotifications);
        notifications.addAll(getUpdateCompiler().getNotifications());
        for (Notification notification : notifications) {
            json.add(notification.createMessageJSON());
        }
        return Json.createObjectBuilder().add("messages", json).build().toString();
    }
}
