package main.java.databaseOperations.exceptions;

/**
 * This exception is thrown whenever an item in the database is corrupted or ruined in some way and
 * needs to be repaired TODO DO?
 */
public class CorruptedItemException extends Exception {
    public CorruptedItemException(String s) {
        super(s);
    }

    public CorruptedItemException(String s, Throwable t) {
        super(s, t);
    }
}
