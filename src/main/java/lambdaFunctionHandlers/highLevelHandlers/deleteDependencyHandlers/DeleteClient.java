package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseObjects.Client;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.EventDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.WorkoutDatabaseActionBuilder;
import main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers.DeleteReview;

import java.util.ArrayList;
import java.util.List;

public class DeleteClient {
    public static List<DatabaseAction> getActions(String clientID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Client client = Client.readClient(clientID);
        // Remove reviews from reviews by and reviews about
        for (String reviewID : client.reviewsBy) {
            // Get all the actions that would be necessary for the review deleting process
            databaseActions.addAll(DeleteReview.getActions(reviewID));
        }
        for (String reviewID : client.reviewsAbout) {
            // Get all the actions that would be necessary for the review deleting process
            databaseActions.addAll(DeleteReview.getActions(reviewID));
        }

        // TODO This should also be able to delete the workout potentially?
        // Remove from all scheduled workouts and completed workouts
        for (String workoutID : client.scheduledWorkouts) {
            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveClient(workoutID, clientID));
            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveMissingReview(workoutID, clientID, false));
        }

        // Also remove from missing reviews in the workouts
        for (String workoutID: client.completedWorkouts) {
            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveClient(workoutID, clientID));
            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveMissingReview(workoutID, clientID, false));
        }

        // Also remove from scheduled events
        for (String eventID : client.scheduledEvents) {
            databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, clientID));
        }

        // Also remove from completed events
        for (String eventID: client.completedEvents) {
            databaseActions.add(EventDatabaseActionBuilder.updateRemoveMember(eventID, clientID));
        }

        // Delete the Client
        databaseActions.add(ClientDatabaseActionBuilder.delete(clientID));

        return databaseActions;
    }
}
