package main.java.databaseOperations;

import main.java.databaseObjects.DatabaseObject;

// TODO CHANGE THIS INTO A LAMBDA FUNCTION?
public interface CheckHandler {
    boolean isViable(DatabaseObject newObject) throws Exception;
}
