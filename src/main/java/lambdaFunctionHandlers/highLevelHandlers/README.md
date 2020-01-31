# High Level Handlers Module Info
This module houses all of the high level function calls that can be seen from the outside world that
can each be called individually from the correct Lambda invocation. Some of them call each other
just to get certain side effects, but anything in this module is meant to be seen by the outside 
world and is in the appropriate documentation outside of this application.

### Organization
This module is split up into
* CREATE (for adding objects in the database)
* DELETE (for removing objects from the database)
* UPDATE SET (for setting string/number values of an object)
* UPDATE ADD (for adding an element to a set of an object / adding to a number)
* UPDATE REMOVE (for removing an element from a set of an object / subtracting from a number)