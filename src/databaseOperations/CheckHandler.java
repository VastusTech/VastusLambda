package databaseOperations;

import databaseObjects.DatabaseObject;

// TODO CHANGE THIS INTO A LAMBDA FUNCTION?
public interface CheckHandler {
    boolean isViable(DatabaseObject newObject) throws Exception;
}
