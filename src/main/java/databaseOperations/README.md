# Database Operations Module Info
This module houses all of the important information related to actually performing actions with the 
database. Handles both mutating and reading of the info.

The most important class here is the DynamoDBHandler, as it has all of the direct logic to the AWS
DynamoDB class and handlers. To handle a transaction, it uses a list of DatabaseActionCompilers, 
which get inputted different individual DatabaseActions and compiles them into the most succinct 
actions that can possibly exist for a DatabaseAction. (This was for efficiency purposes and also if
multiple update actions happened on the same item in a single transaction, an error was thrown.)

The DatabaseActions are created by the DatabaseActionBuilders for each item type in the 
databaseActionBuilders submodule. I put all of this logic so that we know exactly what can and 
cannot be edited in the database at all.

### Database Actions
Each database action specifies all the actions that can be performed on the database by creating
subclasses.

They are separated into
* CREATE (Creating an item)
* UPDATE (Updating an item)
* UPDATESAFE (Updating an item, checking right before to make sure that it's still applicable.)
* DELETE (Deleting an item)
* DELETECONDITIONAL (Deleting an item if the check handler passes right before attempting.)


