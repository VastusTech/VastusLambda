package main.java.lambdaFunctionHandlers.highLevelHandlers.deleteDependencyHandlers;

import main.java.databaseOperations.exceptions.PermissionsException;
import main.java.logic.Constants;
import main.java.databaseObjects.Trainer;
import main.java.databaseOperations.DatabaseAction;
import main.java.databaseOperations.databaseActionBuilders.ClientDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.GymDatabaseActionBuilder;
import main.java.databaseOperations.databaseActionBuilders.TrainerDatabaseActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Deletes a Trainer from the database as well as most dependencies that rely on its TrainerID or
 * its UserID.
 */
public class DeleteTrainer {
    public static List<DatabaseAction> getActions(String fromID, String trainerID) throws Exception {
        List<DatabaseAction> databaseActions = new ArrayList<>();

        Trainer trainer = Trainer.readTrainer(trainerID);

        if (fromID == null || (!fromID.equals(trainerID) && !fromID.equals(trainer.gym)
                && !Constants.isAdmin(fromID))) {
            throw new PermissionsException("You can only delete a trainer if it's yourself or your gym!");
        }

        // Delete the user associated with the trainer
        databaseActions.addAll(DeleteUser.getActions(fromID, trainer));

        if (trainer.gym != null) {
            // Remove from gym's trainers field
            databaseActions.add(GymDatabaseActionBuilder.updateRemoveTrainer(trainer.gym, trainerID));
        }

        // public Set<String> followers;
        // TODO Revisit

        // public Set<String> subscribers;
        for (String subscriberID : trainer.subscribers) {
            databaseActions.add(ClientDatabaseActionBuilder.updateRemoveSubscription(trainerID, subscriberID));
        }

        // Remove all workouts in scheduled workouts and completed workouts (Cancel them)
//        for (String workoutID : trainer.scheduledWorkouts) {
//            databaseActions.addAll(DeleteWorkout.getActions(trainerID, workoutID));
//        }
        // TODO Revisit

        // Delete the Trainer
        databaseActions.add(TrainerDatabaseActionBuilder.delete(trainerID));

        return databaseActions;
    }
}
