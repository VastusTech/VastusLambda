package main.java.databaseOperations.exceptions;

/**
 * This is throws whenever a request attempts to exceed a limit in the database or in a request.
 */
public class ExceedsDatabaseLimitException extends Exception {
    public ExceedsDatabaseLimitException(String s) {
        super(s);
    }

    public ExceedsDatabaseLimitException(String s, Throwable t) {
        super(s, t);
    }
}
