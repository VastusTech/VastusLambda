package main.java.databaseOperations;

import main.java.databaseObjects.DatabaseItem;

/**
 * This defines a CheckHandler that will see if a database item is viable
 */
public interface CheckHandler {
    /**
     * A java lambda function that checks to see if a recently read item is still able to be updated
     * in the fashion specified by the {@link DatabaseAction}.
     *
     * @param newItem The {@link DatabaseItem} read from the database to check for its legitimacy.
     * @return The name of the exception that should be thrown from the failure. Null if fine.
     * @throws Exception If anything unexpected goes wrong during the check.
     */
    String isViable(DatabaseItem newItem) throws Exception;
}
