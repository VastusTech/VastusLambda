package main.java.databaseOperations.exceptions;

/**
 * This exception is thrown whenever an improperly formatted ID is found.
 */
public class BadIDException extends Exception {
    public BadIDException(String s) {
        super(s);
    }

    public BadIDException(String s, Throwable t) {
        super(s, t);
    }
}
