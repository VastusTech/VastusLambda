package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.logic.Constants;
import main.java.databaseObjects.Client;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.WorkoutDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class DeleteClient {
    public static List<DatabaseAction> getActions(String fromID, String clientID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        if (!fromID.equals(clientID) && !fromID.equals(Constants.adminKey)) {
            throw new Exception("PERMISSIONS ERROR: You can only delete a client if it's yourself!");
        }

        // TODO =======================================================================================================
        // TODO We should be deleting far fewer "dependencies" in order to make sure as little info as possible is lost
        // TODO =======================================================================================================

        Client client = Client.readClient(clientID);

        // Delete the user associated with the client
        databaseActions.addAll(DeleteUser.getActions(fromID, client));

        // TODO Don't remove people from workouts, we CAN have empty workouts
        // Remove from all scheduled workouts and completed workouts
        // Also remove from missing reviews in the workouts
        for (String workoutID : client.scheduledWorkouts) {
            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveClient(workoutID, clientID));
            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveMissingReview(workoutID, clientID, false));
        }
//        for (String workoutID: client.completedWorkouts) {
//            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveClient(workoutID, clientID));
//            databaseActions.add(WorkoutDatabaseActionBuilder.updateRemoveMissingReview(workoutID, clientID, false));
//        }


        // Also remove from subscribers in trainers
        for (String subscriptionID: client.subscriptions) {
            databaseActions.add(TrainerDatabaseActionBuilder.updateRemoveSubscriber(subscriptionID, clientID));
        }

        // Delete the Client
        databaseActions.add(ClientDatabaseActionBuilder.delete(clientID));

        return databaseActions;
    }
}
