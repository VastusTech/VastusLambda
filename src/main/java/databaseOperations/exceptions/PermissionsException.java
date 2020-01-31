package main.java.databaseOperations.exceptions;

public class PermissionsException extends Exception {
    public PermissionsException(String s) {
        super(s);
    }

    public PermissionsException(String s, Throwable t) {
        super(s, t);
    }
}
