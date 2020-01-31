package main.java.databaseOperations.exceptions;

/**
 * This exception is thrown whenever an item cannot be found in the database.
 */
public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String s) {
        super(s);
    }

    public ItemNotFoundException(String s, Throwable t) {
        super(s, t);
    }
}
