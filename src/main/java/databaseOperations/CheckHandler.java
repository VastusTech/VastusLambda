package main.java.databaseOperations;

import main.java.databaseObjects.DatabaseItem;

// TODO CHANGE THIS INTO A LAMBDA FUNCTION?
public interface CheckHandler {
    // This checks to see if the new object read from the database is viable
    String isViable(DatabaseItem newItem) throws Exception;
}
