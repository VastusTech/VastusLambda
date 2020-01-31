# Notifications Module Info
This module is in order to handle all of the Ably Real Time notifications, by calling the second 
Lambda function that handles the notification sending. Basically allows you to send the update 
notifications and the Message notifications through their respective channels. 

### How the Ably sending works at a high level
Ably essentially uses their Websocket servers and data centers to make a
subscribe/publish service. So you can publish a message to a topic and anyone subscribed to that 
topic will automatically receive that message through Ably's magic.

### How we use it
We essentially use the Ably Websocket service in order to maintain the real time updated status of 
the objects. Whenever the database is altered in any way, and we think that that data is important 
enough to need to be updated in the client applications, then we will send a message through that 
topic describing the change and giving them the proper information in order to change it. 
Implementation details within...