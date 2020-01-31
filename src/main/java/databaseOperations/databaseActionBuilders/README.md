# Database Action Builders Module Info
This houses all the specific database action builders for each item type to get a specific database 
action for a specific update / action to perform on the database.

Each action builder generally has the create and delete methods and update methods for any 
fields that we want to be able to edit at all in any way. This way, for the high level handlers, 
they simply call a number of builders in the specific action builders which create the 
DatabaseActions that can then be inputted into their respective DatabaseActionCompiler(s).

I also set the precident that each one also has a class "getPrimaryKey" and the field "itemType", 
just to make the code more succinct.