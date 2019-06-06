package main.java.databaseOperations.exceptions;

/**
 * This exception is thrown whenever an incorrect item type is found somewhere.
 */
public class ItemTypeNotRecognizedException extends Exception {
    public ItemTypeNotRecognizedException(String s) {
        super(s);
    }

    public ItemTypeNotRecognizedException(String s, Throwable t) {
        super(s, t);
    }
}
