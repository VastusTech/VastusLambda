# Database Objects Module Info
So I essentially designed this module to store all of the objects representing value objects in the 
database. So this includes all of the item types and the special TimeInterval String value that
stores important time information (using ISO 8601 Time Standard).

These objects include all the potential fields for the objects as well as methods to instantiate
them from the database (the read methods). The read method also uses the DynamoDBHandler and the
DatabaseItemFactory for building the items.

The class structure (besides the Factory and the special TimeInterval object) is essentially that
all classes representing a data type extend the DatabaseItem class, which allows an object to have
a tableName and be read from the database.

Then, most items also extend the DatabaseObject class, which defines an object that exists within 
the "Classics" table (old name for the app lol). This gives the primaryKey for the table and some
extra fields that I wanted all items in the database to have (like time_created and marker).

After that, each item extends the DatabaseObject for being in the "Classics" table and just defines 
their fields, the constructor, the get empty object (for default values), and the read method.

 *Side note, Message is the only item that isn't in the "Classics" table, it's in the "Messages"
 table, thus it extends the DatabaseItem class instead.*
 
 The only other change is that the User object represents multiple different objects that all have
 a User pool in the app and we wanted to have very uniform qualities.