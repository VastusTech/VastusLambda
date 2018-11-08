package main.java.lambdaFunctionHandlers.highLevelHandlers.readHandlers;

import main.java.Logic.Constants;
import main.java.databaseObjects.Event;
import main.java.lambdaFunctionHandlers.responseObjects.EventResponse;
import main.java.lambdaFunctionHandlers.responseObjects.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class ReadEventsByID {
    public static List<ObjectResponse> handle(String[] eventIDs) throws Exception {
        List<ObjectResponse> eventResponses = new ArrayList<>();
        for (String id : eventIDs) {
            Event event = Event.readEvent(id);
            Constants.debugLog("Read event id: " + event.id + "\n");
            EventResponse eventResponse = new EventResponse(event);
            eventResponses.add(eventResponse);
        }
        return eventResponses;
    }
}
