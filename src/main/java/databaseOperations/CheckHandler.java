package main.java.databaseOperations;

import main.java.databaseObjects.DatabaseObject;

// TODO CHANGE THIS INTO A LAMBDA FUNCTION?
public interface CheckHandler {
    // This checks to see if the new object read from the database is viable
    String isViable(DatabaseObject newObject) throws Exception;
}
