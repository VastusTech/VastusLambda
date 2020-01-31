package main.java.databaseOperations;

/**
 * This enum dictates the different kinds of actions a {@link DatabaseAction} can do specifically.
 */
enum DBAction {
    CREATE,
    UPDATE,
    UPDATESAFE,
    DELETE,
    DELETECONDITIONAL
}
