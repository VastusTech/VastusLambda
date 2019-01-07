# Expanding the Data Models

When using these functions, they are highly reliant on the the data model information and what to do with it. If we 
want to expand this, say add more fields or another item type entirely, we need to be very careful and follow a 
step-by-step process to ensure that this process goes as smoothly and quickly as possible.

## Adding a new field

In order to add a new field

Example add workoutHat to Client 

* (First choose both a java name and a database name for it. The java name should be 
camel case 
(thisWouldBeAGoodJavaName) and the database name should be using underscores (this_would_be_a_good_database_name)). 
Whenever there's a call to get from an item or map, you use the database name, whenever you get it from a java 
object, use the java name.
* add definition in Client.java
* add to constructor in Client.java
* add to getEmptyItem in Client.java
* add updateWorkoutHat in ClientDatabaseActionBuilder.java
* add the high-level ClientUpdateWorkoutHat.java in highLevelHandlers/updateSetDependencyHandler and create a 
getActions() method that returns the database actions required to complete the full task (usually just returns the 
low-level databaseAction).
* add definition in CreateClientRequest.java (as a String) under required
* put in constructor and create getter and setter
* go into the CreateClient.java high level function handler and add the check that it's not null in the big `if` 
statement.
* add the setting of it in the ClientDatabaseActionBuilder.java create statement (without a check because it's 
required, but with a check if it is not).
* go into ClientResponse.java and add the definition and the setting in the constructor (as a String or String[]).
* Finally, go into LambdaRequest.java, add the attribute name into the AttributeName enum, and then include it in the
 proper 
 
 #### The Choices involved
 * Decide the name and type of the java object
 * Decide the name and type of the database object
 * Whether or not it is necessary for creation
 * Add a checkHandler if necessary

## Creating a new item type

TBD

## Removing an existing field

TBD

## Removing an item type

TBD